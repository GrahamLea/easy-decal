package org.grlea.games.hl.decal;

// $Id: DecalCreator.java,v 1.2 2005-12-25 22:10:05 grlea Exp $
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
import org.grlea.log.DebugLevel;
import org.grlea.log.SimpleLogger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
DecalCreator
{
   private static final SimpleLogger log = new SimpleLogger(DecalCreator.class);

   private final CreatorResources resources = CreatorResources.instance();

   private final DecalSizes sizes = new DecalSizes();

   // TODO (grahaml) Make this a command line option? Or cfg file?
   private boolean writeMipsToPngs = false;

   private boolean shopMip1InFrame = false;


   public
   DecalCreator()
   {}

   public boolean
   getWriteMipsToPngs()
   {
      return writeMipsToPngs;
   }

   public void
   setWriteMipsToPngs(boolean writeMipsToPngs)
   {
      this.writeMipsToPngs = writeMipsToPngs;
   }

   boolean
   getShopMip1InFrame()
   {
      return shopMip1InFrame;
   }

   void
   setShopMip1InFrame(boolean shopMip1InFrame)
   {
      this.shopMip1InFrame = shopMip1InFrame;
   }

   public void
   createDecals(FileSource fileSource, Callback callback)
   {
      log.entry("createDecals()");

      callback.starting();

      File[] sourceFiles = fileSource.getSourceFiles();
      log.ludicrousObject("sourceFiles", sourceFiles);

      for (int i = 0; i < sourceFiles.length; i++)
      {
         File imageFile = sourceFiles[i];
         log.verbose("Loading image");
         log.debugObject("imageFile", imageFile);
         callback.c1_StartingFile(imageFile);

         try
         {
            callback.c2_Reading();

            if (!imageFile.exists())
            {
               log.debug("File doesn't exist");
               callback.error(resources.fileDoesntExist(imageFile));
               continue;
            }

            if (imageFile.isDirectory())
            {
               log.debug("File is a directory");
               callback.error(resources.fileIsDirectory(imageFile));
               continue;
            }

            if (!imageFile.canRead())
            {
               log.debug("File does not have read permission");
               callback.error(resources.fileCannotBeRead(imageFile));
               continue;
            }

            // Read image in
            final BufferedImage image;
            try
            {
               image = ImageIO.read(imageFile);

               if (image == null)
               {
                  log.debug("Failed to read image");
                  callback.error(resources.javaCannotReadImage());
                  continue;
               }
            }
            catch (IOException e)
            {
               callback.error(resources.failureWhileReadingImage(e));
               continue;
            }

            // Find best size
            log.verbose("Calculating decal size");
            callback.c3_CalculatingSize();
            DecalSize size = sizes.getClosestDimension(image);
            log.debugObject("size", size);
            callback.c4_Resizing(size);

            log.verbose("Creating WAD entry");
            WadEntry entry = new WadEntry("pldecal", size.getWidth(), size.getHeight(), image);
            log.verboseObject("entry", entry);

            // Create palette
            log.verbose("Generating palette");
            callback.c5_GeneratingPalette();
            entry.generatePaletteFromAllMips();

            // Write to WAD
            log.verbose("Creating WAD file");
            File directory = imageFile.getParentFile();
            File wadFile = new File(directory, imageFile.getName() + ".wad");
            log.debugObject("wadFile", wadFile);
            callback.c6_WritingWad(wadFile);
            Wad wad = new Wad();
            wad.addEntry(entry);

            try
            {
               log.verbose("Writing WAD file");
               wad.write(wadFile);
            }
            catch (IOException e)
            {
               log.warn("Error writing WAD file");
               log.dbe(DebugLevel.L5_DEBUG, e);
               callback.error(resources.failureWhileWritingWadFile(e));
               continue;
            }

            // Test code to write a PNG file of MIP1

            if (writeMipsToPngs)
            {
               log.debug("Writing MIPs to files");
               try
               {
                  callback.c65_WritingMipsToPngs();
                  String mip1Filename = wadFile.getName() + ".mip1.png";
                  String mip2Filename = wadFile.getName() + ".mip2.png";
                  String mip3Filename = wadFile.getName() + ".mip3.png";
                  String mip4Filename = wadFile.getName() + ".mip4.png";
                  boolean imagesWritten;
                  imagesWritten = ImageIO.write(entry.getMip1Converted(), "png", new File(directory, mip1Filename));
                  imagesWritten &= ImageIO.write(entry.getMip2Converted(), "png", new File(directory, mip2Filename));
                  imagesWritten &= ImageIO.write(entry.getMip3Converted(), "png", new File(directory, mip3Filename));
                  imagesWritten &= ImageIO.write(entry.getMip4Converted(), "png", new File(directory, mip4Filename));
                  if (!imagesWritten)
                     throw new IOException("Failed to find PNG writer.");
               }
               catch (IOException e)
               {
                  log.warn("Failed to write MIPs to files");
                  log.dbe(DebugLevel.L5_DEBUG, e);
                  callback.error(resources.failureWhileWritingPngFiles(e));
                  continue;
               }
            }

            // Test code to show MIP1 in a frame

            if (shopMip1InFrame)
            {
               ImageIcon icon = new ImageIcon(entry.getMip1Converted());
               JFrame frame = new JFrame(imageFile.getName());
               frame.getContentPane().add(new JButton(icon));
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.pack();
               frame.setVisible(true);
            }
         }
         catch (RuntimeException e)
         {
            log.error("RuntimeException in DecalCreator");
            log.warnException(e);
            callback.uncaughtError(e);
            continue;
         }
         catch (Error e)
         {
            log.error("java.lang.Error in DecalCreator");
            log.warnException(e);
            callback.uncaughtError(e);
            continue;
         }

         callback.c7_ImageDone();
      }

      log.verbose("Done");
      callback.allDone();

      log.exit("createDecals()");
   }

   public static interface
   Callback
   {
      public void
      starting();

      public void
      c1_StartingFile(File file);

      public void
      c2_Reading();

      public void
      c3_CalculatingSize();

      public void
      c4_Resizing(DecalSize decalSize);

      public void
      c5_GeneratingPalette();

      public void
      c6_WritingWad(File wadFile);

      public void
      c65_WritingMipsToPngs();

      public void
      c7_ImageDone();

      public void
      allDone();

      public void
      error(String error);

      public void
      uncaughtError(Throwable t);
   }
}