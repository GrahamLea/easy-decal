package org.grlea.games.hl.decal;

// $Id: DecalCreator.java,v 1.1 2004-11-26 12:27:37 grlea Exp $
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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
DecalCreator
{
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
      File[] sourceFiles = fileSource.getSourceFiles();

      for (int i = 0; i < sourceFiles.length; i++)
      {
         File imageFile = sourceFiles[i];
         callback.c1_StartingFile(imageFile);
         try
         {
            callback.c2_Reading();

            if (!imageFile.exists())
            {
               callback.error(imageFile + " does not exist.");
               continue;
            }

            if (imageFile.isDirectory())
            {
               callback.error(imageFile + " is a directory.");
               continue;
            }

            if (!imageFile.canRead())
            {
               callback.error(imageFile + " cannot be read.");
               continue;
            }

            // Read image in
            BufferedImage image = null;
            try
            {
               image = ImageIO.read(imageFile);

               if (image == null)
               {
                  callback.error("Your Java installation is not capable of reading this type of image.");
                  continue;
               }
            }
            catch (IOException e)
            {
               callback.error("Failure while reading image file: " + e);
               continue;
            }

            // Find best size
            callback.c3_CalculatingSize();
            DecalSize size = sizes.getClosestDimension(image);
            callback.c4_Resizing(size);

            WadEntry entry = new WadEntry("pldecal", size.getWidth(), size.getHeight(), image);

            // Create palette
            callback.c5_GeneratingPalette();
            entry.generatePaletteFromAllMips();

            // Write to WAD
            File directory = imageFile.getParentFile();
            File wadFile = new File(directory, imageFile.getName() + ".wad");
            callback.c6_WritingWad(wadFile);
            Wad wad = new Wad();
            wad.addEntry(entry);

            try
            {
               wad.write(wadFile);
            }
            catch (IOException e)
            {
               callback.error("Failure while writing WAD file: " + e);
               continue;
            }

            // Test code to write a PNG file of MIP1

            if (writeMipsToPngs)
            {
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
                  callback.error("Failure while writing PNG file(s): " + e);
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
            callback.uncaughtError(e);
            continue;
         }
         catch (Error e)
         {
            callback.uncaughtError(e);
            continue;
         }

         callback.c7_ImageDone();
      }

      callback.allDone();
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

      // TODO (grahaml) Internationalize this:
      public void
      error(String error);

      public void
      uncaughtError(Throwable t);
   }
}