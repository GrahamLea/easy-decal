package org.grlea.games.hl.decal.gui;

// $Id: Model.java,v 1.1 2005-12-25 22:10:10 grlea Exp $
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

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
Model
{
   private final SourceFileList fileList = new SourceFileList();

   private final Processor processor = new Processor();

   public
   Model()
   {
   }

   public SourceFileList
   getFileList()
   {
      return fileList;
   }

   public Processor
   getProcessor()
   {
      return processor;
   }
}