package org.grlea.games.hl.decal;

// $Id: DecalSizes.java,v 1.1 2004-11-25 05:07:18 grlea Exp $
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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
DecalSizes
{
   private static final int MAX_DIMENSION = 256;
   private static final int MAX_FACTOR = MAX_DIMENSION / DecalSize.DIMENSION_FACTOR;

   private final List validSizes;

   public
   DecalSizes()
   {
      int[] allowableDimensions = new int[MAX_FACTOR];
      for (int i = 1; i <= MAX_FACTOR; i++)
         allowableDimensions[i - 1] = i * DecalSize.DIMENSION_FACTOR;

      List newValidSizes = new ArrayList(MAX_FACTOR * MAX_FACTOR / 2);

      for (int a = 0; a < allowableDimensions.length; a++)
      {
         int width = allowableDimensions[a];
         for (int b = 0; b < allowableDimensions.length; b++)
         {
            int height = allowableDimensions[b];
            if ((width * height) <= DecalSize.MAX_AREA)
            {
               try
               {
                  newValidSizes.add(new DecalSize(width, height));
               }
               catch (IllegalDecalSizeException e)
               {
                  throw new RuntimeException("Illegal dimension generated: " + e);
               }
            }
         }
      }

      validSizes = Collections.unmodifiableList(newValidSizes);
   }

   public DecalSize
   getClosestDimension(BufferedImage image)
   {
      // Find the two dimensions that this image's ratio sits between
      DecalSize bestScaleSize = null;
      float bestScaleRatio = 0;
      int bestScaleArea = 0;
      for (Iterator iter = validSizes.iterator(); iter.hasNext();)
      {
         DecalSize size = (DecalSize) iter.next();
         float scaleRatio = size.getScaleRatio(image);
         // We want the size that will have the biggest (scaled) size and the least wasted space
         if (scaleRatio > bestScaleRatio || (scaleRatio == bestScaleRatio && size.getArea() < bestScaleArea))
         {
            bestScaleSize = size;
            bestScaleRatio = scaleRatio;
            bestScaleArea = size.getArea();
         }
      }

      return bestScaleSize;
   }
}