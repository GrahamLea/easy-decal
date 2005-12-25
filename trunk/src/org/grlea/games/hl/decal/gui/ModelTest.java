package org.grlea.games.hl.decal.gui;

// $Id: ModelTest.java,v 1.1 2005-12-25 22:10:10 grlea Exp $
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
import java.util.ArrayList;

/**
 * <p></p>
 *
 * @author grlea
 * @version $Revision: 1.1 $
 */
public class
ModelTest
{
   public
   ModelTest()
   {
   }

   public static void
   main(String[] args)
   {
      Model model = new Model();
      SourceFileList fileList = model.getFileList();

      final ArrayList filesAdded = new ArrayList();
      final ArrayList filesRemoved = new ArrayList();

      SourceFileList.Listener listener = new SourceFileList.Listener() {
         public void
         fileAdded(SourceFile file)
         {
            filesAdded.add(file);
         }

         public void
         fileRemoved(SourceFile file)
         {
            filesRemoved.add(file);
         }
      };

      fileList.addListener(listener);

      File bartFile = new File("images/decals/bartSimpson.jpg");
      SourceFile bartSourceFile1 = new SourceFile(bartFile);
      SourceFile bartSourceFile2 = new SourceFile(bartFile);

      fileList.addFile(bartSourceFile1);
      fileList.addFile(bartSourceFile2);
      fileList.removeFile(bartSourceFile2);

      assert filesAdded.contains(bartSourceFile1);
      assert filesAdded.contains(bartSourceFile2);

      assert filesRemoved.contains(bartSourceFile2);
      assert !filesRemoved.contains(bartSourceFile1);
   }
}