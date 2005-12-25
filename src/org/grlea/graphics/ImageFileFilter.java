package org.grlea.graphics;

// $Id: ImageFileFilter.java,v 1.1 2005-12-25 22:10:13 grlea Exp $
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

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * <p>A file filter that 'accepts' all image types known to the {@link ImageIO} system.</p>
 *
 * @author Graham Lea (<a href="mailto:grlea@dev.java.net">grlea@dev.java.net</a>)
 * @version $Revision: 1.1 $
 */
public class 
ImageFileFilter
extends FileFilter
implements FilenameFilter
{
   /** The name of the file filter, displayed in the file chooser's list of filters. */
   private static final String DESCRIPTION = "All Images ";

   /** The set of image extensions understood by the ImageIO framework. */
   private final HashSet imageExtensions;

   /** A description of this image filter and the extensions it can open. */
   private final String description;

   /**
    * Creates and initialises a new <code>ImageFileFilter</code>.
    */
   public
   ImageFileFilter()
   {
      imageExtensions = new HashSet(10, 0.85F);
      TreeSet lowerCaseExtensions = new TreeSet();

      String[] knownExtensions = ImageIO.getReaderFormatNames();
      for (int i = 0; i < knownExtensions.length; i++)
      {
         imageExtensions.add(knownExtensions[i]);
         lowerCaseExtensions.add(knownExtensions[i].toLowerCase());
      }

      StringBuffer descriptionBuffer = new StringBuffer(DESCRIPTION).append('(');
      boolean first = true;
      for (Iterator iter = lowerCaseExtensions.iterator(); iter.hasNext();)
      {
         if (first)
            first = false;
         else
            descriptionBuffer.append(";");

         descriptionBuffer.append("*.").append(iter.next());
      }

      descriptionBuffer.append(')');
      description = descriptionBuffer.toString();
   }

   /**
    * Returns true if the given file has an extension that can be opened by ImageIO.
    */
   public boolean
   accept(File file)
   {
      if (file.isDirectory())
         return true;

      String filename = file.getName();
      int lastPeriod = filename.lastIndexOf('.');
      if (lastPeriod == -1)
         return false;

      String extension = filename.substring(lastPeriod + 1);

      return imageExtensions.contains(extension);
   }

   /**
    * Returns true if the given file has an extension that can be opened by ImageIO.
    */
   public boolean
   accept(File dir, String name)
   {
      return accept(new File(dir, name));
   }

   /**
    * Returns a description and summary of the types accepted by this filter.
    */
   public String
   getDescription()
   {
      return description;
   }
}