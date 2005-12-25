package org.grlea.games.hl.decal.batch;

// $Id: EasyDecalBatchProcessor.java,v 1.1 2005-12-25 22:10:06 grlea Exp $
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

import org.grlea.games.hl.decal.CommonResources;
import org.grlea.games.hl.decal.DecalCreator;
import org.grlea.games.hl.decal.DecalSize;
import org.grlea.games.hl.decal.EasyDecal;
import org.grlea.games.hl.decal.FileSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
EasyDecalBatchProcessor
implements FileSource, DecalCreator.Callback
{
   private static final String CONFIG_FILE_NAME = EasyDecal.PROJECT_NAME + ".cfg";

   private static final String KEY_WRITE_MIPS = "writeMips";

   private final File[] imageFiles;

   private final CommonResources commonResources;
   private final BatchResources batchResources;

   private boolean errors = false;

   public
   EasyDecalBatchProcessor(String[] filenames)
   {
      imageFiles = new File[filenames.length];
      commonResources = CommonResources.instance();
      batchResources = BatchResources.instance();
      for (int i = 0; i < filenames.length; i++)
      {
         imageFiles[i] = new File(filenames[i]);
      }
   }

   public File[]
   getSourceFiles()
   {
      return imageFiles;
   }

   public void
   starting()
   {
      System.out.println(batchResources.beginningBatch());
      System.out.println();
   }

   public void
   c1_StartingFile(File file)
   {
      System.out.println(file.getAbsolutePath());
   }

   public void
   c2_Reading()
   {
      System.out.print("   ");
      System.out.println(commonResources.reading());
   }

   public void
   c3_CalculatingSize()
   {
      System.out.print("   ");
      System.out.print(commonResources.converting());
      System.out.print(" ");
   }

   public void
   c4_Resizing(DecalSize decalSize)
   {
      System.out.println("(" + decalSize.getWidth() + "x" + decalSize.getHeight() + ")");
   }

   public void
   c5_GeneratingPalette()
   {
   }

   public void
   c6_WritingWad(File wadFile)
   {
      System.out.print("   ");
      System.out.println(commonResources.writingWad());
   }

   public void
   c65_WritingMipsToPngs()
   {
      System.out.print("   ");
      System.out.println(batchResources.writingMipsToPngs());
   }

   public void
   c7_ImageDone()
   {
      System.out.print("   ");
      System.out.println(commonResources.done());
      System.out.println();
   }

   public void
   allDone()
   {
      if (errors)
      {
         System.err.println();
         System.err.print("*** ");
         System.err.print(batchResources.warning());
         System.err.println(" ***");
         System.err.println();
         System.err.println(batchResources.errorsDuringBatch());
         System.err.println(batchResources.seeOutput());
         System.err.println();
         System.exit(-1);
      }
      System.exit(0);
   }

   public void
   error(String error)
   {
      System.err.println();
      System.err.print("   ");
      System.err.println(batchResources.error(error));
      System.err.println();
      errors = true;
   }

   public void
   uncaughtError(Throwable t)
   {
      System.err.println();
      System.err.print("   ");
      System.err.print(batchResources.uncaughtRuntime());
      System.err.print(": " + t);
      System.err.println(t);
      t.printStackTrace();
      System.err.println();
      errors = true;
   }

   public static void
   startEasyDecalBatch(String[] argv)
   {
      Properties configuration = new Properties();
      File configFile = new File(CONFIG_FILE_NAME);
      if (configFile.exists() && configFile.canRead())
      {
         try
         {
            configuration.load(new FileInputStream(configFile));
         }
         catch (IOException e)
         {
            System.out.println();
            System.out.println(CommonResources.instance().errorReadingConfigFile(e));
            System.out.println();
         }
      }

      EasyDecalBatchProcessor batchProcessor = new EasyDecalBatchProcessor(argv);
      DecalCreator creator = new DecalCreator();

      if ("true".equals(configuration.getProperty(KEY_WRITE_MIPS, "").toLowerCase()))
         creator.setWriteMipsToPngs(true);

      creator.createDecals(batchProcessor, batchProcessor);
   }
}