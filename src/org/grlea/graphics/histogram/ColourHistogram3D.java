package org.grlea.graphics.histogram;

// $Id: ColourHistogram3D.java,v 1.2 2005-12-25 22:10:14 grlea Exp $
// Copyright (c) 2004 Graham Lea. All rights reserved.

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import org.grlea.graphics.ColourHelp;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p></p>
 *
 * @author grahaml
 * @version $Revision: 1.2 $
 */
public class
ColourHistogram3D
{
   public static final int MAX_DEPTH = 8;

   private static final int DEFAULT_LEAF_LIST_SIZE = 256 * 256;

   private final int depth;
   private final Branch histogramRoot = new Branch(null, -1);
   private final ArrayList leaves;

   public
   ColourHistogram3D()
   {
      this(MAX_DEPTH);
   }

   public
   ColourHistogram3D(int depth)
   {
      this(depth, null);
   }

   public
   ColourHistogram3D(BufferedImage image)
   {
      this(MAX_DEPTH, image);
   }

   public
   ColourHistogram3D(int depth, BufferedImage image)
   {
      this.depth = depth;
      if (image == null)
      {
         leaves = new ArrayList(DEFAULT_LEAF_LIST_SIZE);
      }
      else
      {
         leaves = new ArrayList(image.getWidth() * image.getHeight() * (depth / MAX_DEPTH));
         insertSamples(image);
      }
   }

   public void
   insertSamples(BufferedImage image)
   {
      int width = image.getWidth();
      int height = image.getHeight();
      int[] samples = new int[width * height];
      image.getRGB(0, 0, width, height, samples, 0, width);
      for (int i = 0; i < samples.length; i++)
      {
         int sample = samples[i];
         insertSample(ColourHelp.getRed(sample), ColourHelp.getGreen(sample), ColourHelp.getBlue(sample));
      }
   }

   private void
   insertSample(int r, int g, int b)
   {
      int rgb = (r << 16) | (g << 8) | b;

      Branch branch = histogramRoot;
      for (int bit = 7, lastBit = 8 - depth; bit >= lastBit; bit--)
      {
         int mask = 0x01 << bit;
         int key = ((r & mask) >> (bit - 2)) | ((g & mask) >> (bit - 1)) | ((b & mask) >> bit);

         if (bit == lastBit)
         {
            Leaf leaf = (Leaf) branch.children[key];
            if (leaf == null)
            {
               leaf = new Leaf(branch, key, rgb);
               branch.children[key] = leaf;
               leaves.add(leaf);
            }
            leaf.samples++;
         }
         else
         {
            Branch nextBranch = (Branch) branch.children[key];
            if (nextBranch == null)
            {
               nextBranch = new Branch(branch, key);
               branch.children[key] = nextBranch;
            }
            branch = nextBranch;
         }
      }
   }

   public void
   getColours(ColourSampleProcessor processor)
   {
      for (Iterator iter = leaves.iterator(); iter.hasNext();)
      {
         Leaf leaf = (Leaf) iter.next();
         processor.samples(leaf.getRGB(), leaf.samples);
      }
   }

   public void
   getColours(ColourComponentsProcessor processor)
   {
      for (Iterator iter = leaves.iterator(); iter.hasNext();)
      {
         Leaf leaf = (Leaf) iter.next();
         processor.samples(leaf.getRed(), leaf.getGreen(), leaf.getBlue(), leaf.samples);
      }
   }

   public void
   clear()
   {
      for (int i = 0; i < histogramRoot.children.length; i++)
      {
         histogramRoot.children[i] = null;
      }
      leaves.clear();
   }

   private static abstract class
   TreePart
   {
      private final TreePart parent;
      final int key;

      public
      TreePart(TreePart parent, int key)
      {
         this.parent = parent;
         this.key = key;
      }
   }

   private static final class
   Branch
   extends TreePart
   {
      private TreePart[] children = new TreePart[8];

      public
      Branch(Branch parent, int key)
      {
         super(parent, key);
      }
   }

   private static final class
   Leaf
   extends TreePart
   {
      private int samples = 0;
      private int rgb;

      public
      Leaf(Branch parent, int key, int rgb)
      {
         super(parent, key);
         this.rgb = rgb;
      }

      public int
      getRGB()
      {
         return rgb;
      }

      public int
      getRed()
      {
         return (rgb & 0x00FF0000) >> 16;
      }

      public int
      getGreen()
      {
         return (rgb & 0x0000FF00) >> 8;
      }

      public int
      getBlue()
      {
         return (rgb & 0x000000FF);
      }
   }
}