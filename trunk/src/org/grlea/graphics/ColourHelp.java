package org.grlea.graphics;

// $Id: ColourHelp.java,v 1.1 2004-11-25 05:07:16 grlea Exp $
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



/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
ColourHelp
{
   public static final int ALPHA_MASK = 0x00FF0000;
   public static final int RED_MASK = 0x00FF0000;
   public static final int GREEN_MASK = 0x0000FF00;
   public static final int BLUE_MASK = 0x000000FF;

   public static final int ALPHA_SHIFT = 24;
   public static final int RED_SHIFT = 16;
   public static final int GREEN_SHIFT = 8;
   public static final int BLUE_SHIFT = 0;

   private static final int FF = 0xFF;

   private
   ColourHelp()
   {}

   public static int
   getAlpha(int argb)
   {
      return (argb & ALPHA_MASK) >> ALPHA_SHIFT;
   }

   public static int
   getRed(int argb)
   {
      return (argb & RED_MASK) >> RED_SHIFT;
   }

   public static int
   getGreen(int argb)
   {
      return (argb & GREEN_MASK) >> GREEN_SHIFT;
   }

   public static int
   getBlue(int argb)
   {
      return (argb & BLUE_MASK) >> BLUE_SHIFT;
   }

   public static final int
   getRgb(int a, int r, int g, int b)
   {
      return ((a & FF) << ALPHA_SHIFT) | ((r & FF) << RED_SHIFT) | ((g & FF) << GREEN_SHIFT) | ((b & FF) << BLUE_SHIFT); 
   }

   public static final int
   getRgb(int r, int g, int b)
   {
      return ((r & FF) << RED_SHIFT) | ((g & FF) << GREEN_SHIFT) | ((b & FF) << BLUE_SHIFT);
   }
}