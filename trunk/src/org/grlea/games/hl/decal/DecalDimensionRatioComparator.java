package org.grlea.games.hl.decal;

// $Id: DecalDimensionRatioComparator.java,v 1.1 2004-11-25 05:07:18 grlea Exp $
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

import java.util.Comparator;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
DecalDimensionRatioComparator
implements Comparator
{
   public
   DecalDimensionRatioComparator()
   {}

   public int
   compare(Object o1, Object o2)
   {
      DecalSize decalDimension1 = (DecalSize) o1;
      DecalSize decalDimension2 = (DecalSize) o2;
      float diff = decalDimension1.getRatio() - decalDimension2.getRatio();
      if (diff < 0)
         return -1;
      else if (diff > 0)
         return 1;
      else
         return 0;
   }
}