package org.grlea.games.hl.decal.gui;

// $Id: Processor.java,v 1.1 2005-12-25 22:10:10 grlea Exp $
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

import org.grlea.games.hl.decal.DecalCreator;
import org.grlea.games.hl.decal.DecalSize;
import org.grlea.games.hl.decal.FileSource;
import org.grlea.log.SimpleLogger;

import java.awt.EventQueue;
import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
Processor
{
   private static final SimpleLogger log = new SimpleLogger(Processor.class);

   private final List listeners = new ArrayList();

   public
   Processor()
   {
   }

   public void
   createDecals(Model model)
   {
      log.entry("createDecals()");

      DecalCreator decalCreator = new DecalCreator();
      DecalCreator.Callback listenerMultiplexor = (DecalCreator.Callback)
         Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{DecalCreator.Callback.class},
                                new ListenerMultiplexor());

      decalCreator.createDecals(new ModelFileSource(model), listenerMultiplexor);

      log.exit("createDecals()");
   }

   public void
   addListener(Listener listener)
   {
      log.entry("addListener()");
      listeners.add(listener);
      log.exit("addListener()");
   }

   public void
   removeListener(Listener listener)
   {
      log.entry("removeListener()");
      listeners.remove(listener);
      log.exit("removeListener()");
   }

   private static class
   ModelFileSource
   implements FileSource
   {
      private final Model model;

      public
      ModelFileSource(Model model)
      {
         this.model = model;
      }

      public File[]
      getSourceFiles()
      {
         log.entry("getSourceFiles()");

         SourceFileList sourceFiles = model.getFileList();
         int numberOfFiles = sourceFiles.getSize();
         File[] files = new File[numberOfFiles];
         for (int i = 0; i < numberOfFiles; i++)
         {
            files[i] = sourceFiles.getFile(i).getFile();
         }

         log.exit("getSourceFiles()");
         return files;
      }
   }

   public abstract static class
   Listener
   implements DecalCreator.Callback
   {
      public void
      starting()
      {
      }

      public void
      c1_StartingFile(File file)
      {
      }

      public void
      c2_Reading()
      {
      }

      public void
      c3_CalculatingSize()
      {
      }

      public void
      c4_Resizing(DecalSize decalSize)
      {
      }

      public void
      c5_GeneratingPalette()
      {
      }

      public void
      c6_WritingWad(File wadFile)
      {
      }

      public void
      c65_WritingMipsToPngs()
      {
      }

      public void
      c7_ImageDone()
      {
      }

      public void
      allDone()
      {
      }

      public void
      error(String error)
      {
      }

      public void
      uncaughtError(Throwable t)
      {
      }
   }

   private final class
   ListenerMultiplexor
   implements InvocationHandler
   {
      private final SimpleLogger log = new SimpleLogger(ListenerMultiplexor.class);

      public Object
      invoke(Object proxy, final Method method, final Object[] args)
      throws Throwable
      {
         if (log.isTracing())
            log.entry(method.getName() + "()");

         for (Iterator iterator = listeners.iterator(); iterator.hasNext();)
         {
            final Listener listener = (Listener) iterator.next();
            EventQueue.invokeLater(new Runnable()
            {
               public void
               run()
               {
                  try
                  {
                     method.invoke(listener, args);
                  }
                  catch (IllegalAccessException e)
                  {
                     log.error("Error invoking " + method.getName() + "() on " + listener.getClass().getName());
                     log.errorException(e);
                  }
                  catch (InvocationTargetException e)
                  {
                     log.error("Error invoking " + method.getName() + "() on " + listener.getClass().getName());
                     log.errorException(e);
                  }
               }
            });
         }

         if (log.isTracing())
            log.exit(method.getName() + "()");  

         return null;
      }
   }
}