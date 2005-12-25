package org.grlea.games.hl.decal.gui;

// $Id: EasyDecalModel.java,v 1.1 2005-12-25 22:10:08 grlea Exp $
// Copyright (c) 2003 Forge Research Pty Ltd. All rights reserved.
// www.forge.com.au

import org.grlea.games.hl.decal.DecalCreator;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>A model for Decal-producing GUI components</p>
 *
 * @author Graham Lea
 * @version $Revision: 1.1 $
 */
public class
EasyDecalModel
{
   private final DecalCreator decalCreator;

   private final FileListModel fileListModel;

   public
   EasyDecalModel(DecalCreator decalCreator)
   {
      if (decalCreator == null)
         throw new IllegalArgumentException("decalCreator cannot be null.");

      this.decalCreator = decalCreator;
      fileListModel = new FileListModel();
   }

   public ListModel
   getFileListModel()
   {
      return fileListModel;
   }

   private final class
   FileListModel
   extends AbstractListModel
   {
      private final List files;

      public
      FileListModel()
      {
         this.files = new ArrayList(2);
      }

      public boolean
      addFile(File file)
      {
         if (!files.contains(file))
         {
            files.add(file);
            int index = files.size() - 1;
            fireIntervalAdded(this, index, index);
            return true;
         }
         else
         {
            return false;
         }
      }

      public boolean
      addFiles(File[] fileArray)
      {
         int startIndex = files.size() - 1;
         boolean addedOne = true;

         for (int i = 0; i < fileArray.length; i++)
         {
            File file = fileArray[i];

            if (!files.contains(file))
            {
               files.add(file);
               if (!addedOne)
                  addedOne = true;
            }
         }

         if (addedOne)
         {
            int endIndex = files.size() - 1;
            fireIntervalAdded(this, startIndex, endIndex);
            return true;
         }
         else
         {
            return false;
         }
      }

      public boolean
      removeFile(File file)
      {
         if (files.remove(file))
         {
            int index = files.size();
            fireIntervalRemoved(this, index, index);
            return true;
         }
         else
         {
            return false;
         }
      }

      public boolean
      removeFiles(File[] fileArray)
      {
         // This is too hard to optimise (i.e. to remove multiple fireIntervalRemoved() calls)
         // because the elements being removed aren't necessarily sequential.
         boolean removedOne = false;
         for (int i = 0; i < fileArray.length; i++)
         {
            if (removeFile(fileArray[i]) && !removedOne)
               removedOne = true;
         }

         return removedOne;
      }

      public int
      getSize()
      {
         return files.size();
      }

      public Object
      getElementAt(int index)
      {
         return files.get(index);
      }
   }
}