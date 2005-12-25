package org.grlea.games.hl.decal.gui;

// $Id: SourceFileList.java,v 1.1 2005-12-25 22:10:10 grlea Exp $
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
SourceFileList
{
   private final List files = new ArrayList();

   private final List listeners = new ArrayList();

   public
   SourceFileList()
   {
   }

   public void
   addListener(Listener listener)
   {
      listeners.add(listener);
   }

   public void
   removeListener(Listener listener)
   {
      listeners.remove(listener);
   }

   public void
   addFile(SourceFile file)
   {
      files.add(file);
      fireListenerAdded(file);
   }

   public void
   removeFile(SourceFile file)
   {
      files.remove(file);
      fireListenerRemoved(file);
   }

   private void
   fireListenerAdded(SourceFile file)
   {
      for (Iterator iterator = listeners.iterator(); iterator.hasNext();)
      {
         ((Listener) iterator.next()).fileAdded(file);
      }
   }

   private void
   fireListenerRemoved(SourceFile file)
   {
      for (Iterator iterator = listeners.iterator(); iterator.hasNext();)
      {
         ((Listener) iterator.next()).fileRemoved(file);
      }
   }

   public int
   getSize()
   {
      return files.size();
   }

   public SourceFile
   getFile(int index)
   {
      return (SourceFile) files.get(index);
   }

   public static interface
   Listener
   {
      public void
      fileAdded(SourceFile file);

      public void
      fileRemoved(SourceFile file);
   }
}