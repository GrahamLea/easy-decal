package org.grlea.games.hl.decal;

// $Id: EasyDecal.java,v 1.1 2004-11-25 05:07:18 grlea Exp $
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
EasyDecal
{
   private static final String VERSION = "0.1a";

   public static void
   main(String[] argv)
   {
      System.out.println();
      System.out.println("Easy Decal v" + VERSION);
      System.out.println();

      // Read arguments
      if (argv.length == 0)
         usage();

      System.out.println("Beginning batch processing");
      EasyDecalBatchProcessor.main(argv);
   }

   private static void
   usage()
   {
      System.err.println("usage: <jvm> " + EasyDecal.class.getName() + " <image_file> [<image_file> ...]");
      System.exit(1);
   }
}