package org.grlea.graphics.quantization;

// $Id: PopularityColourQuantizer.java,v 1.1 2004-11-25 05:07:17 grlea Exp $
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
import org.grlea.graphics.histogram.ColourHistogram3D;
import org.grlea.graphics.histogram.ColourSampleProcessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
PopularityColourQuantizer
implements ColourQuantizer
{
   private final ArrayList requiredColours = new ArrayList(2);

   private final ColourHistogram3D histogram;

   public
   PopularityColourQuantizer(ColourHistogram3D histogram)
   {
      this.histogram = histogram;
   }

   public void
   addRequiredColour(Color colour)
   {
      requiredColours.add(colour);
   }

   public void
   insertSamples(BufferedImage image)
   {
      histogram.insertSamples(image);
   }

   public int[]
   getPalette(int colours)
   {
      int[] palette = new int[colours];
      final TreeSet colourCounts = new TreeSet();
      histogram.getColours(new ColourSampleProcessor() {
         public void
         samples(int rgb, int samples)
         {
            colourCounts.add(new ColourCount(rgb, samples));
         }
      });

      final int coloursToPick = colours - requiredColours.size();
      int index = 0;
      for (Iterator iter = colourCounts.iterator(); iter.hasNext();)
      {
         ColourCount colourCount = (ColourCount) iter.next();
         palette[index++] = colourCount.rgb;
         if (index == coloursToPick)
            break;
      }
      int reverseIndex = colours - 1;
      for (Iterator iter = requiredColours.iterator(); iter.hasNext();)
      {
         palette[reverseIndex--] = ((Color) iter.next()).getRGB();
      }

//      if (index != (reverseIndex + 1))
//         throw new IllegalStateException("Colour mismatch: index = " + index + ", reverseIndex = " + reverseIndex);

      return palette;
   }

   public void
   reset()
   {
      histogram.clear();
      requiredColours.clear();
   }

   private static final class
   ColourCount
   implements Comparable
   {
      private int rgb;
      private int samples;

      public
      ColourCount(int rgb, int samples)
      {
         this.rgb = rgb;
         this.samples = samples;
      }

      public int
      compareTo(Object o)
      {
         ColourCount colourCount = (ColourCount) o;

         if (samples != colourCount.samples)
            return colourCount.samples - samples;

         int red1 = ColourHelp.getRed(rgb);
         int red2 = ColourHelp.getRed(colourCount.rgb);
         if (red1 != red2)
            return red2 - red1;

         int green1 = ColourHelp.getGreen(rgb);
         int green2 = ColourHelp.getGreen(colourCount.rgb);
         if (green1 != green2)
            return green2 - green1;

         int blue1 = ColourHelp.getBlue(rgb);
         int blue2 = ColourHelp.getBlue(colourCount.rgb);
         if (blue1 != blue2)
            return blue2 - blue1;

         return 0;
      }
   }
}