package org.grlea.games.hl.decal;

// $Id: DecalSize.java,v 1.1 2004-11-25 05:07:18 grlea Exp $
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

import org.grlea.graphics.RenderingHintsHelp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
DecalSize
{
   public static final int MAX_AREA = 10752;

   public static final int DIMENSION_FACTOR = 16;

   private final int width;

   private final int height;

   private final float ratio;

   public
   DecalSize(int width, int height)
   throws IllegalDecalSizeException
   {
      if (width % DIMENSION_FACTOR != 0 || height % DIMENSION_FACTOR != 0 || width * height > MAX_AREA)
         throw new IllegalDecalSizeException(width, height);

      this.width = width;
      this.height = height;
      ratio = (float) width / height;
   }

   public int
   getWidth()
   {
      return width;
   }

   public int
   getHeight()
   {
      return height;
   }

   public int
   getArea()
   {
      return width * height;
   }

   public float
   getRatio()
   {
      return ratio;
   }

   public float
   getScaleRatio(BufferedImage image)
   {
      // Use the smallest scale ratio to fit the whole image in.
      float widthScaleRatio = (float) width / image.getWidth();
      float heightScaleRatio = (float) height / image.getHeight();
      float scaleRatio = Math.min(widthScaleRatio, heightScaleRatio);
      return scaleRatio;
   }

   public BufferedImage
   scale(BufferedImage image)
   {
      int imageWidth = image.getWidth();
      int imageHeight = image.getHeight();

      float scaleRatio;
      if (imageWidth <= width && imageHeight <= height)
      {
         scaleRatio = 1;
      }
      else
      {
         float widthScaleRatio = (float) width / imageWidth;
         float heightScaleRatio = (float) height / imageHeight;
         scaleRatio = Math.min(widthScaleRatio, heightScaleRatio);
      }

      // Calculate new width and height, and the offsets required to centre the image.
      int newWidth = (int) (imageWidth * scaleRatio);
      int newHeight = (int) (imageHeight * scaleRatio);
      int xOffset = (int) (((width - newWidth) / 2));
      int yOffset = (int) (((height - newHeight) / 2));

      Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_AREA_AVERAGING);

      BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics = result.createGraphics();
      graphics.setRenderingHints(RenderingHintsHelp.quality());
      graphics.setColor(Color.BLUE);
      graphics.fillRect(0, 0, width, height);
      graphics.drawImage(scaledImage, xOffset, yOffset, null);
      graphics.dispose();

      return result;
   }
}