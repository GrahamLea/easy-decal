package org.grlea.graphics.quantization;

// $Id: MedianCutColourQuantizer.java,v 1.1 2004-11-25 05:07:17 grlea Exp $
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
import org.grlea.graphics.histogram.ColourComponentsProcessor;
import org.grlea.graphics.histogram.ColourHistogram3D;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
MedianCutColourQuantizer
implements ColourQuantizer
{
   private final ArrayList requiredColours = new ArrayList(2);

   private final ColourHistogram3D histogram;

   public
   MedianCutColourQuantizer()
   {
      this(new ColourHistogram3D());
   }

   public
   MedianCutColourQuantizer(ColourHistogram3D histogram)
   {
      this.histogram = histogram;
   }

   public void
   reset()
   {
      histogram.clear();
      requiredColours.clear();
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
      // Create a colour cube
      final ColourCube initialCube = new ColourCube();
      histogram.getColours(new ColourComponentsProcessor()
      {
         public void
         samples(int r, int g, int b, int samples)
         {
            initialCube.addColourCount(new ColourCount(r, g, b, samples));
         }
      });

      // Split cubes until we have the required number of colours
      TreeSet colourCubes = new TreeSet();
      colourCubes.add(initialCube);
      final int coloursToPick = colours - requiredColours.size();
      while (colourCubes.size() < coloursToPick)
      {
         ColourCube cubeToSplit = (ColourCube) colourCubes.first();
         if (!cubeToSplit.canBeSplit())
            break;

         if (!colourCubes.remove(cubeToSplit))
            throw new IllegalStateException("Failed to remove cube!");
         ColourCube[] newCubes = cubeToSplit.split();
         for (int i = 0; i < newCubes.length; i++)
         {
            colourCubes.add(newCubes[i]);
         }
      }

      // Create the palette
      int index = 0;
      int[] palette = new int[colours];
      for (Iterator iter = colourCubes.iterator(); iter.hasNext();)
      {
         palette[index++] = ((ColourCube) iter.next()).getColour();
      }

      int reverseIndex = colours - 1;
      for (Iterator iter = requiredColours.iterator(); iter.hasNext();)
      {
         palette[reverseIndex--] = ((Color) iter.next()).getRGB();
      }

      return palette;
   }

   private static final class
   ColourCube
   implements Comparable
   {
      private final Set colourCounts = new HashSet();

      private int redMin;
      private int redMax;
      private int greenMin;
      private int greenMax;
      private int blueMin;
      private int blueMax;
      private int sampleCount = 0;

      public
      ColourCube()
      {}

      public void
      addColourCount(ColourCount count)
      {
         // Add the object
         colourCounts.add(count);

         // Increment the number of samples
         sampleCount += count.samples;

         // Adjust the min and max as necessary
         if (colourCounts.size() == 1)
         {
            redMin = count.red;
            redMax = count.red;
            greenMin = count.green;
            greenMax = count.green;
            blueMin = count.blue;
            blueMax = count.blue;
         }
         else
         {
            if (count.red < redMin)
               redMin = count.red;
            if (count.red > redMax)
               redMax = count.red;
            if (count.green < greenMin)
               greenMin = count.green;
            if (count.green > greenMax)
               greenMax = count.green;
            if (count.blue < blueMin)
               blueMin = count.blue;
            if (count.blue > blueMax)
               blueMax = count.blue;
         }
      }


      public ColourCube[]
      split()
      {
         // Order the samples along the axis with the largest range
         // TODO (grahaml) Option to choose dimension by # colours or # samples, rather than range
         int redRange = getRedRange();
         int greenRange = getGreenRange();
         int blueRange = getBlueRange();

         int maxRange = Math.max(redRange, Math.max(greenRange, blueRange));

         Comparator comparator;
         if (maxRange == greenRange)
            comparator = ColourCount.greenComparator;
         else if (maxRange == redRange)
            comparator = ColourCount.redComparator;
         else
            comparator = ColourCount.blueComparator;

         TreeSet orderedColours = new TreeSet(comparator);
         orderedColours.addAll(colourCounts);

         // Find the median sample while placing the colours each side into new cubes
         // TODO (grahaml) Option to find median colour, rather than sample
         int medianSample = sampleCount / 2;
         int samplesInLowerCube = 0;
         ColourCube lowerCube = new ColourCube();
         ColourCube higherCube = new ColourCube();
         boolean lowerThanMedian = true;
         ColourCount medianColour = null;
         for (Iterator iter = orderedColours.iterator(); iter.hasNext();)
         {
            ColourCount colourCount = (ColourCount) iter.next();
            if (lowerThanMedian)
            {
               medianColour = colourCount;
               if (medianSample - samplesInLowerCube < colourCount.samples)
               {
                  lowerThanMedian = false;
                  continue;
               }
               lowerCube.addColourCount(colourCount);
               samplesInLowerCube += colourCount.samples;
            }
            else
            {
               higherCube.addColourCount(colourCount);
            }
         }

         // Place the median colour in the first or second half.
         int samplesLowerThanMedianColour = samplesInLowerCube;
         int samplesHigherThanMedianColour = sampleCount - samplesInLowerCube - medianColour.samples;
         if (samplesLowerThanMedianColour < samplesHigherThanMedianColour)
            lowerCube.addColourCount(medianColour);
         else
            higherCube.addColourCount(medianColour);

         if (lowerCube.colourCounts.isEmpty() || higherCube.colourCounts.isEmpty())
            throw new IllegalStateException("Empty cube created.");

         return new ColourCube[] {lowerCube, higherCube};
      }

      public boolean
      canBeSplit()
      {
         return colourCounts.size() > 1;
      }

      private int
      getRedRange()
      {
         return redMax - redMin;
      }

      private int
      getGreenRange()
      {
         return greenMax - greenMin;
      }

      private int
      getBlueRange()
      {
         return blueMax - blueMin;
      }

      public int
      compareTo(Object object)
      {
         ColourCube that = (ColourCube) object;
         if (this == that)
            return 0;

         int thisRedRange = getRedRange();
         int thisGreenRange = getGreenRange();
         int thisBlueRange = getBlueRange();
         int thatRedRange = that.getRedRange();
         int thatGreenRange = that.getGreenRange();
         int thatBlueRange = that.getBlueRange();

         // Compare the maximum ranges
         int thisMaxRange = Math.max(thisRedRange, Math.max(thisGreenRange, thisBlueRange));
         int thatMaxRange = Math.max(thatRedRange, Math.max(thatGreenRange, thatBlueRange));

         boolean thisMaxRangeIsRed = thisMaxRange == thisRedRange;
         boolean thisMaxRangeIsGreen = thisMaxRange == thisGreenRange;
         boolean thisMaxRangeIsBlue = thisMaxRange == thisBlueRange;
         boolean thatMaxRangeIsRed = thatMaxRange == thatRedRange;
         boolean thatMaxRangeIsGreen = thatMaxRange == thatGreenRange;
         boolean thatMaxRangeIsBlue = thatMaxRange == thatBlueRange;


         if (thisMaxRange > thatMaxRange)
            return -32;
         else if (thisMaxRange < thatMaxRange)
            return 32;


         // Compare number of colours
         if (colourCounts.size() > that.colourCounts.size())
            return -16;
         if (colourCounts.size() < that.colourCounts.size())
            return 16;

         // Compare number of samples
         if (sampleCount > that.sampleCount)
            return -8;
         if (sampleCount < that.sampleCount)
            return 8;

         // Compare overall ranges
         int thisTotalRange = thisRedRange + thisGreenRange + thisBlueRange;
         int thatTotalRange = thatRedRange + thatGreenRange + thatBlueRange;
         if (thisTotalRange > thatTotalRange)
            return -4;
         if (thisTotalRange < thatTotalRange)
            return 4;

         // Compare maximum values (favouring brighter colours)
         if (thisMaxRangeIsGreen && thatMaxRangeIsGreen)
         {
            if (greenMax > that.greenMax)
               return -2;
            if (greenMax < that.greenMax)
               return 2;
         }
         if (thisMaxRangeIsRed && thatMaxRangeIsRed)
         {
            if (redMax > that.redMax)
               return -2;
            if (redMax < that.redMax)
               return 2;
         }
         if (thisMaxRangeIsBlue && thatMaxRangeIsBlue)
         {
            if (blueMax > that.blueMax)
               return -2;
            if (blueMax < that.blueMax)
               return 2;
         }

         if (thisMaxRangeIsGreen && !thatMaxRangeIsGreen)
            return -1;
         if (!thisMaxRangeIsGreen && thatMaxRangeIsGreen)
            return 1;
         if (thisMaxRangeIsRed && !thatMaxRangeIsRed)
            return -1;
         if (!thisMaxRangeIsRed && thatMaxRangeIsRed)
            return 1;
         if (thisMaxRangeIsBlue && !thatMaxRangeIsBlue)
            return -1;
         if (!thisMaxRangeIsBlue && thatMaxRangeIsBlue)
            return 1;

         // Nothing left to compare (but don't want to admit "equality")
         return hashCode() - that.hashCode();
      }

      public int
      getColour()
      {
         // Find the average colour of the cube
         // TODO (grahaml) Option to use the colour furthest from a centre colour?
         int red = 0;
         int green = 0;
         int blue = 0;
         for (Iterator iter = colourCounts.iterator(); iter.hasNext();)
         {
            ColourCount colourCount = (ColourCount) iter.next();
            red += colourCount.red * colourCount.samples;
            green += colourCount.green * colourCount.samples;
            blue += colourCount.blue * colourCount.samples;
         }

         return ColourHelp.getRgb(red / sampleCount, green / sampleCount, blue / sampleCount);
      }
   }

   private static final class
   ColourCount
   {

      private static final Comparator redComparator = new Comparator()
      {
         public int
         compare(Object o1, Object o2)
         {
            ColourCount count1 = (ColourCount) o1;
            ColourCount count2 = (ColourCount) o2;

            if (count1 == count2)
               return 0;

            if (count1.red != count2.red)
               return count1.red - count2.red;

            int sum1 = count1.green + count1.blue;
            int sum2 = count2.green + count2.blue;
            if (sum1 != sum2)
               return sum1 - sum2;

            if (count1.green != count2.green)
               return count1.green - count2.green;

            if (count1.blue != count2.blue)
               return count1.blue - count2.blue;

            return 0;
         }
      };

      private static final Comparator greenComparator = new Comparator()
      {
         public int
         compare(Object o1, Object o2)
         {
            ColourCount count1 = (ColourCount) o1;
            ColourCount count2 = (ColourCount) o2;

            if (count1 == count2)
               return 0;

            if (count1.green != count2.green)
               return count1.green - count2.green;

            int sum1 = count1.red + count1.blue;
            int sum2 = count2.red + count2.blue;
            if (sum1 != sum2)
               return sum1 - sum2;

            if (count1.red != count2.red)
               return count1.red - count2.red;

            if (count1.blue != count2.blue)
               return count1.blue - count2.blue;

            return 0;
         }
      };

      private static final Comparator blueComparator = new Comparator()
      {
         public int
         compare(Object o1, Object o2)
         {
            ColourCount count1 = (ColourCount) o1;
            ColourCount count2 = (ColourCount) o2;

            if (count1 == count2)
               return 0;

            if (count1.blue != count2.blue)
               return count1.blue - count2.blue;

            int sum1 = count1.green + count1.red;
            int sum2 = count2.green + count2.red;
            if (sum1 != sum2)
               return sum1 - sum2;

            if (count1.green != count2.green)
               return count1.green - count2.green;

            if (count1.red != count2.red)
               return count1.red - count2.red;

            return 0;
         }
      };

      private int red;
      private int green;
      private int blue;
      private int samples;

      public
      ColourCount(int red, int green, int blue, int samples)
      {
         this.red = red;
         this.green = green;
         this.blue = blue;
         this.samples = samples;
      }
   }
}