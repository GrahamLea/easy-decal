package org.grlea.games.hl.wad;

// $Id: WadEntry.java,v 1.2 2004-11-26 12:27:38 grlea Exp $
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
import org.grlea.graphics.quantization.ColourQuantizer;
import org.grlea.graphics.quantization.MedianCutColourQuantizer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;


/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.2 $
 */
public class
WadEntry
{
   public static final int MAX_NAME_LENGTH = 16;

   public static final int MIP2_DIMENSION_FACTOR = 2;
   public static final int MIP2_AREA_FACTOR = MIP2_DIMENSION_FACTOR * MIP2_DIMENSION_FACTOR;

   public static final int MIP3_DIMENSION_FACTOR = 4;
   public static final int MIP3_AREA_FACTOR = MIP3_DIMENSION_FACTOR * MIP3_DIMENSION_FACTOR;

   public static final int MIP4_DIMENSION_FACTOR = 8;
   public static final int MIP4_AREA_FACTOR = MIP4_DIMENSION_FACTOR * MIP4_DIMENSION_FACTOR;

   private static final boolean CREATE_MIP_DEFAULT_CENTRE = true;
   private static final boolean CREATE_MIP_DEFAULT_SCALE = true;
   private static final boolean CREATE_MIP_DEFAULT_STRETCH = false;

   public static final int WAD3_ENTRY_HEADER_LENGTH = 40;

   private String name;

   private final int width;
   private final int height;

   private BufferedImage mip1 = null;
   private BufferedImage mip2 = null;
   private BufferedImage mip3 = null;
   private BufferedImage mip4 = null;

   private int[] palette;
   private IndexColorModel indexColorModel;

   private BufferedImage mip1Converted = null;
   private BufferedImage mip2Converted = null;
   private BufferedImage mip3Converted = null;
   private BufferedImage mip4Converted = null;

   public
   WadEntry(String name, int width, int height)
   {
      if (width % 16 != 0)
         throw new IllegalArgumentException("Width of a WadEntry must be a multiple of 16.");
      if (height % 16 != 0)
         throw new IllegalArgumentException("Height of a WadEntry must be a multiple of 16.");

      if (width > 256)
         throw new IllegalArgumentException("Width of a WadEntry must be a less than 256.");
      if (height > 256)
         throw new IllegalArgumentException("Height of a WadEntry must be a less than 256.");

      setName(name);
      this.width = width;
      this.height = height;
   }

   public
   WadEntry(String name, int width, int height, BufferedImage image)
   {
      this(name, width, height);
      createAllMips(image);
   }

   public BufferedImage
   getMip1()
   {
      return mip1;
   }

   public BufferedImage
   getMip2()
   {
      return mip2;
   }

   public BufferedImage
   getMip3()
   {
      return mip3;
   }

   public BufferedImage
   getMip4()
   {
      return mip4;
   }

   public void
   createMip1(BufferedImage image)
   {
      createMip1(image, CREATE_MIP_DEFAULT_CENTRE, CREATE_MIP_DEFAULT_SCALE, CREATE_MIP_DEFAULT_STRETCH);
   }

   public void
   createMip2(BufferedImage image)
   {
      createMip2(image, CREATE_MIP_DEFAULT_CENTRE, CREATE_MIP_DEFAULT_SCALE, CREATE_MIP_DEFAULT_STRETCH);
   }

   public void
   createMip3(BufferedImage image)
   {
      createMip3(image, CREATE_MIP_DEFAULT_CENTRE, CREATE_MIP_DEFAULT_SCALE, CREATE_MIP_DEFAULT_STRETCH);
   }

   public void
   createMip4(BufferedImage image)
   {
      createMip4(image, CREATE_MIP_DEFAULT_CENTRE, CREATE_MIP_DEFAULT_SCALE, CREATE_MIP_DEFAULT_STRETCH);
   }

   public void
   createAllMips(BufferedImage image)
   {
      createAllMips(image, CREATE_MIP_DEFAULT_CENTRE, CREATE_MIP_DEFAULT_SCALE, CREATE_MIP_DEFAULT_STRETCH);
   }

   public void
   createMip1(BufferedImage image, boolean centre, boolean scale, boolean stretch)
   {
      setMip1(transform(image, width, height, centre, scale, stretch));
   }

   public void
   createMip2(BufferedImage image, boolean centre, boolean scale, boolean stretch)
   {
      setMip2(transform(image, width / MIP2_DIMENSION_FACTOR, height / MIP2_DIMENSION_FACTOR, centre, scale, stretch));
   }

   public void
   createMip3(BufferedImage image, boolean centre, boolean scale, boolean stretch)
   {
      setMip3(transform(image, width / MIP3_DIMENSION_FACTOR, height / MIP3_DIMENSION_FACTOR, centre, scale, stretch));
   }

   public void
   createMip4(BufferedImage image, boolean centre, boolean scale, boolean stretch)
   {
      setMip4(transform(image, width / MIP4_DIMENSION_FACTOR, height / MIP4_DIMENSION_FACTOR, centre, scale, stretch));
   }

   public void
   createAllMips(BufferedImage image, boolean centre, boolean scale, boolean stretch)
   {
      createMip1(image, centre, scale, stretch);
      createMip2(image, centre, scale, stretch);
      createMip3(image, centre, scale, stretch);
      createMip4(image, centre, scale, stretch);
   }

   private BufferedImage
   transform(BufferedImage image, int width, int height, boolean centre, boolean scale, boolean stretch)
   {
      Image sourceImage = image;
      int sourceImageWidth = image.getWidth();
      int sourceImageHeight = image.getHeight();

      if (scale)
      {
         int newWidth;
         int newHeight;
         if (stretch)
         {
            newWidth = width;
            newHeight = height;
         }
         else
         {
            float xScaleFactor = (float) width / image.getWidth();
            float yScaleFactor = (float) height / image.getHeight();
            float scaleFactor = Math.min(xScaleFactor, yScaleFactor);
            newWidth = (int) (image.getWidth() * scaleFactor);
            newHeight = (int) (image.getHeight() * scaleFactor);
         }

         sourceImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_AREA_AVERAGING);
         sourceImageWidth = newWidth;
         sourceImageHeight = newHeight;
      }

      int xOffset = 0;
      int yOffset = 0;
      if (centre)
      {
         xOffset = (width - sourceImageWidth) / 2;
         yOffset = (height - sourceImageHeight) / 2;
      }

      BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics = resultImage.createGraphics();
      graphics.drawImage(sourceImage, xOffset, yOffset, null);
      graphics.dispose();
      return resultImage;
   }

   public void
   generateLowerMips()
   {
      if (mip1 == null)
         throw new IllegalStateException("MIP 1 has not been created.");
      createMip2(mip1);
      createMip3(mip1);
      createMip4(mip1);
   }

   public void
   generatePaletteFromMip1()
   {
      generatePaletteFromMip1(new MedianCutColourQuantizer());
   }

   public void
   generatePaletteFromMip1(ColourQuantizer quantizer)
   {
      if (mip1 == null)
         throw new IllegalStateException("MIP 1 has not been created.");

      quantizer.reset();
      quantizer.addRequiredColour(Color.blue);
      quantizer.insertSamples(mip1);
      setPalette(quantizer.getPalette(256));
   }

   public void
   generatePaletteFromAllMips()
   {
      generatePaletteFromAllMips(new MedianCutColourQuantizer());
   }

   public void
   generatePaletteFromAllMips(ColourQuantizer quantizer)
   {
      if (mip1 == null)
         throw new IllegalStateException("MIP 1 has not been created.");
      createLowerMipsAsNecessary();

      quantizer.reset();
      quantizer.addRequiredColour(Color.blue);
      quantizer.insertSamples(mip1);
      quantizer.insertSamples(mip2);
      quantizer.insertSamples(mip3);
      quantizer.insertSamples(mip4);
      setPalette(quantizer.getPalette(256));
   }

   void
   write(ByteBuffer buffer)
   throws IOException
   {
      if (mip1 == null)
         throw new IllegalStateException("MIP 1 has not been created.");

      createLowerMipsAsNecessary();
      createPaletteIfNecessary();

      // Name, width and height
      writeName(buffer, false);
      buffer.putInt(width);
      buffer.putInt(height);

      int mip1Size = getMip1Area();
      int mip2Size = getMip2Area();
      int mip3Size = getMip3Area();
      int mip4Size = getMip4Area();

      // Mip Offsets (relative to that start of the entry)
      int mipOffset = WAD3_ENTRY_HEADER_LENGTH;
      buffer.putInt(mipOffset);
      mipOffset += mip1Size;
      buffer.putInt(mipOffset);
      mipOffset += mip2Size;
      buffer.putInt(mipOffset);
      mipOffset += mip3Size;
      buffer.putInt(mipOffset);

      // MIP data
      byte[] buf = new byte[mip1Size];
      buffer.put(getMip1Data(buf), 0, mip1Size);
      buffer.put(getMip2Data(buf), 0, mip2Size);
      buffer.put(getMip3Data(buf), 0, mip3Size);
      buffer.put(getMip4Data(buf), 0, mip4Size);

      // Palette
      buffer.putShort((short) palette.length);
      for (int i = 0; i < palette.length; i++)
      {
         int rgb = palette[i];
         buffer.put((byte) ((rgb & 0xFF0000) >> 16));
         buffer.put((byte) ((rgb & 0x00FF00) >> 8));
         buffer.put((byte) ((rgb & 0x0000FF)));
      }

      // Padding
      buffer.put((byte) 0);
      buffer.put((byte) 0);
   }

   public IndexColorModel
   getColourModel()
   {
      if (indexColorModel == null)
      {
         createPaletteIfNecessary();
         indexColorModel = new IndexColorModel(3, 256, palette, 0, false, -1, DataBuffer.TYPE_BYTE);
      }
      return indexColorModel;
   }

   public BufferedImage
   getMip1Converted()
   {
      if (mip1Converted == null)
      {
         if (mip1 == null)
            throw new IllegalStateException("MIP 1 has not been created.");
         mip1Converted = convertImage(mip1);
      }
      return mip1Converted;
   }

   public BufferedImage
   getMip2Converted()
   {
      if (mip2Converted == null)
      {
         if (mip2 == null)
            createMip2(mip1);
         mip2Converted = convertImage(mip2);
      }
      return mip2Converted;
   }

   public BufferedImage
   getMip3Converted()
   {
      if (mip3Converted == null)
      {
         if (mip3 == null)
            createMip3(mip1);
         mip3Converted = convertImage(mip3);
      }
      return mip3Converted;
   }

   public BufferedImage
   getMip4Converted()
   {
      if (mip4Converted == null)
      {
         if (mip4 == null)
            createMip4(mip1);
         mip4Converted = convertImage(mip4);
      }
      return mip4Converted;
   }

   private BufferedImage
   convertImage(BufferedImage sourceImage)
   {
      int newWidth = sourceImage.getWidth();
      int newHeight = sourceImage.getHeight();
      BufferedImage convertedImage =
         new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_INDEXED, getColourModel());
      Graphics2D graphics = convertedImage.createGraphics();
      graphics.addRenderingHints(RenderingHintsHelp.quality());
      graphics.drawImage(sourceImage, 0, 0, Color.blue, null);
      graphics.dispose();
      graphics = null;
      return convertedImage;
   }

   private int[]
   createPaletteIfNecessary()
   {
      if (palette == null)
         generatePaletteFromMip1();
      return palette;
   }

   void
   writeName(ByteBuffer buffer, boolean upperCase)
   throws IOException
   {
      String nameToPrint = upperCase ? name.toUpperCase() : name.toLowerCase();
      try
      {
         byte[] nameBytes = nameToPrint.getBytes("US-ASCII");
         if (nameBytes.length > WadEntry.MAX_NAME_LENGTH)
            throw new IOException("Entry name longer than 16 ASCII characters ('" + nameToPrint + "')");
         buffer.put(nameBytes);
         // Pad with zeroes:
         int zeroesRequired = WadEntry.MAX_NAME_LENGTH - nameBytes.length;
         while (zeroesRequired-- > 0)
            buffer.put((byte) 0);
      }
      catch (UnsupportedEncodingException e)
      {
         throw new IOException("Failed to convert entry name to ASCII ('" + nameToPrint + "'): " + e);
      }
   }

   private void
   createLowerMipsAsNecessary()
   {
      if (mip2 == null)
         createMip2(mip1);
      if (mip3 == null)
         createMip3(mip1);
      if (mip4 == null)
         createMip4(mip1);
   }

   public void
   setMip1(BufferedImage mipImage)
   {
      if (mipImage.getWidth() != width || mipImage.getHeight() != height)
         throw new IllegalArgumentException("MIP 1's width and height must be the same as the WadEntry's.");
      this.mip1 = mipImage;
      mip1Converted = null;
   }

   public void
   setMip2(BufferedImage mipImage)
   {
      if (mipImage.getWidth() != width / MIP2_DIMENSION_FACTOR ||
          mipImage.getHeight() != height / MIP2_DIMENSION_FACTOR)
      {
         throw new IllegalArgumentException("MIP 2's width and height must be the WadEntry's width and height " +
                                            "divided by " + MIP2_DIMENSION_FACTOR);
      }
      this.mip2 = mipImage;
      mip2Converted = null;
   }

   public void
   setMip3(BufferedImage mipImage)
   {
      if (mipImage.getWidth() != width / MIP3_DIMENSION_FACTOR ||
          mipImage.getHeight() != height / MIP3_DIMENSION_FACTOR)
      {
         throw new IllegalArgumentException("MIP 3's width and height must be the WadEntry's width and height " +
                                            "divided by " + MIP3_DIMENSION_FACTOR);
      }
      this.mip3 = mipImage;
      mip3Converted = null;
   }

   public void
   setMip4(BufferedImage mipImage)
   {
      if (mipImage.getWidth() != width / MIP4_DIMENSION_FACTOR ||
          mipImage.getHeight() != height / MIP4_DIMENSION_FACTOR)
      {
         throw new IllegalArgumentException("MIP 4's width and height must be the WadEntry's width and height " +
                                            "divided by " + MIP4_DIMENSION_FACTOR);
      }
      this.mip4 = mipImage;
      mip4Converted = null;
   }

   public byte[]
   getMip1Data(byte[] buffer)
   {
      return getMipData(getMip1Converted(), buffer);
   }

   public byte[]
   getMip2Data(byte[] buffer)
   {
      return getMipData(getMip2Converted(), buffer);
   }

   public byte[]
   getMip3Data(byte[] buffer)
   {
      return getMipData(getMip3Converted(), buffer);
   }

   public byte[]
   getMip4Data(byte[] buffer)
   {
      return getMipData(getMip4Converted(), buffer);
   }

   private byte[]
   getMipData(BufferedImage image, byte[] buffer)
   {
      int imageWidth = image.getWidth();
      int imageHeight = image.getHeight();
      int[] samples = new int[imageWidth * imageHeight];
      image.getData().getSamples(0, 0, imageWidth, imageHeight, 0, samples);
      for (int i = 0; i < samples.length; i++)
      {
         buffer[i] = (byte) (samples[i] & 0xFF);
      }
      return buffer;
   }

   public String
   getName()
   {
      return name;
   }

   public void
   setName(String newName)
   {
      if (newName == null)
         throw new IllegalArgumentException("name cannot be null.");
      if (newName.length() > MAX_NAME_LENGTH)
         throw new IllegalArgumentException("name must <= " + MAX_NAME_LENGTH + " characters.");
      this.name = newName;
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

   public int[]
   getPalette()
   {
      return palette;
   }

   public void
   setPalette(int[] palette)
   {
      this.palette = palette;
      indexColorModel = null;
      mip1Converted= null;
      mip2Converted= null;
      mip3Converted= null;
      mip4Converted= null;
   }

   public int
   getPaletteSize()
   {
      return palette.length;
   }

   public int
   getMip1Area()
   {
      return width * height;
   }

   public int
   getMip2Area()
   {
      return width * height / MIP2_AREA_FACTOR;
   }

   public int
   getMip3Area()
   {
      return width * height / MIP3_AREA_FACTOR;
   }

   public int
   getMip4Area()
   {
      return width * height / MIP4_AREA_FACTOR;
   }
}