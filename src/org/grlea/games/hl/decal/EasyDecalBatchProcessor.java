package org.grlea.games.hl.decal;

// $Id: EasyDecalBatchProcessor.java,v 1.2 2004-11-26 12:27:37 grlea Exp $
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

import org.grlea.games.hl.wad.Wad;
import org.grlea.games.hl.wad.WadEntry;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
class
EasyDecalBatchProcessor
implements FileSource, DecalCreator.Callback
{
   private final File[] imageFiles;

   private boolean errors = false;

   public
   EasyDecalBatchProcessor(String[] filenames)
   {
      imageFiles = new File[filenames.length];
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
      System.out.println("Beginning batch processing");
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
      // TODO (grahaml) Internationalize this
      System.out.println("   Reading");
   }

   public void
   c3_CalculatingSize()
   {
      // TODO (grahaml) Internationalize this
      System.out.print("   Converting ");
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
      // TODO (grahaml) Internationalize this
      System.out.println("   Writing WAD");
   }

   public void
   c65_WritingMipsToPngs()
   {
      // TODO (grahaml) Internationalize this
      System.out.println("   Writing MIPs to PNG files");
   }

   public void
   c7_ImageDone()
   {
      // TODO (grahaml) Internationalize this
      System.out.println("   Done");
      System.out.println();
   }

   public void
   allDone()
   {
      // TODO (grahaml) Internationalize this
      if (errors)
      {
         System.err.println();
         System.err.println("*** WARNING *** ");
         System.err.println();
         System.err.println("Errors occurred during batch processing.");
         System.err.println("See output for details.");
         System.err.println();
         System.exit(-1);
      }
      System.exit(0);
   }

   public void
   error(String error)
   {
      // TODO (grahaml) Internationalize this
      System.err.println();
      System.err.println("   ERROR: " + error);
      System.err.println();
      errors = true;
   }

   public void
   uncaughtError(Throwable t)
   {
      // TODO (grahaml) Internationalize this
      System.err.println();
      System.err.println("   UNCAUGHT RUNTIME ERROR: " + t);
      t.printStackTrace();
      System.err.println();
      errors = true;
   }
}