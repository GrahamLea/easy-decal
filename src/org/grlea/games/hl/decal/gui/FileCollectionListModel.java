package org.grlea.games.hl.decal.gui;

// $Id: FileCollectionListModel.java,v 1.1 2005-12-25 22:10:08 grlea Exp $
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

import javax.swing.AbstractListModel;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
FileCollectionListModel
extends AbstractListModel
implements SourceFileList.Listener
{
   private final SourceFileList fileList;

   public
   FileCollectionListModel(Model model)
   {
      fileList = model.getFileList();
      fileList.addListener(this);
   }

   public int
   getSize()
   {
      return fileList.getSize();
   }

   public Object
   getElementAt(int index)
   {
      return fileList.getFile(index);
   }

   public void
   fileAdded(SourceFile file)
   {
      fireContentsChanged(this, 0, getSize());
   }

   public void
   fileRemoved(SourceFile file)
   {
      fireContentsChanged(this, 0, getSize());
   }
}