package org.grlea.games.hl.wad;

// $Id: Wad.java,v 1.1 2004-11-25 05:07:19 grlea Exp $
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
Wad
{
   public static final int WAD3_HEADER_LENGTH = 12;
   public static final int WAD3_DIRECTORY_ENTRY_LENGTH = 32;
   public static final int WAD3_ENTRY_HEADER_LENGTH = 40;
   public static final int WAD3_ENTRY_EXTRA_BYTES = 4;

   private static final byte[] MAGIC_NUMBER = new byte[] {'W', 'A', 'D', '3'};

   private static final char WAD3_ENTRY_TYPE_MIP = 'C';

   private final List wadEntries;

   public
   Wad()
   {
      wadEntries = new ArrayList(2);
   }

   public void
   addEntry(WadEntry entry)
   {
      if (entry == null)
         throw new IllegalArgumentException("entry cannot be null.");
      wadEntries.add(entry);
   }

   public void
   removeEntry(WadEntry entry)
   {
      wadEntries.remove(entry);
   }

   public void
   removeEntry(String entryName)
   {
      for (Iterator iter = wadEntries.iterator(); iter.hasNext();)
      {
         if (((WadEntry) iter.next()).getName().equals(entryName))
         {
            iter.remove();
            break;
         }
      }
   }

   public List
   getEntries()
   {
      return new ArrayList(wadEntries);
   }

   public void
   write(File file)
   throws IOException
   {
      FileOutputStream fileOut = new FileOutputStream(file);
      write(fileOut);
      fileOut.close();
   }

   public void
   write(FileOutputStream out)
   throws IOException
   {
      int wadFileSize = calculateWadFileSize();
      ByteBuffer buffer = ByteBuffer.allocate(wadFileSize);
      buffer.order(ByteOrder.LITTLE_ENDIAN);

      writeHeader(wadFileSize, buffer);
      for (Iterator iter = wadEntries.iterator(); iter.hasNext();)
      {
         ((WadEntry) iter.next()).write(buffer);
      }

      writeDirectory(buffer);

      buffer.flip();
      out.getChannel().write(buffer);
      out.flush();
   }

   private void
   writeHeader(int wadFileSize, ByteBuffer buffer)
   {
      buffer.put(MAGIC_NUMBER);
      buffer.putInt(wadEntries.size());
      buffer.putInt(wadFileSize - calculateWadDirectorySize());
   }

   private void
   writeDirectory(ByteBuffer buffer)
   throws IOException
   {
      int entryOffset = WAD3_HEADER_LENGTH;
      for (Iterator iter = wadEntries.iterator(); iter.hasNext();)
      {
         WadEntry entry = (WadEntry) iter.next();
         int entrySize = calculateWadEntrySize(entry);

         // Offset of the entry
         buffer.putInt(entryOffset);
         entryOffset += entrySize;

         // Entry sizes (on disk and in memory)
         buffer.putInt(entrySize);
         buffer.putInt(entrySize);

         // Entry type, compression type
         buffer.put((byte) WAD3_ENTRY_TYPE_MIP);
         buffer.put((byte) 0);

         // Padding
         buffer.put((byte) 0);
         buffer.put((byte) 0);

         // Entry name
         entry.writeName(buffer, true);
      }
   }

   private int
   calculateWadFileSize()
   {
      int wadSize = WAD3_HEADER_LENGTH + calculateWadDirectorySize();
      for (Iterator iter = wadEntries.iterator(); iter.hasNext();)
      {
         wadSize += calculateWadEntrySize((WadEntry) iter.next());
      }
      return wadSize;
   }

   private int
   calculateWadDirectorySize()
   {
      return wadEntries.size() * WAD3_DIRECTORY_ENTRY_LENGTH;
   }

   private int
   calculateWadEntrySize(WadEntry wadEntry)
   {
      int width = wadEntry.getWidth();
      int height = wadEntry.getHeight();
      int area = width * height;
      return WAD3_ENTRY_HEADER_LENGTH +
               area +
               (area / WadEntry.MIP2_AREA_FACTOR) +
               (area / WadEntry.MIP3_AREA_FACTOR) +
               (area / WadEntry.MIP4_AREA_FACTOR) +
               WAD3_ENTRY_EXTRA_BYTES +
               (wadEntry.getPaletteSize() * 3);
   }
}