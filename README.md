A Refactored version of Roman Nuriks [UndoBar](http://code.google.com/p/romannurik-code/source/browse/misc/undobar)
=======

All credits to him!

Changes : 
======

1. Undobar is now typed ``new UndoBar<String>()``
2. Can be inflated into a specific ViewGroup or attached to a activity parent layout.
3. Turned into a APKLib for easy importing
4. Removed the saving and restore state (for now)
5. Made a simple demo

Usage
====

To show - 

    new UndoBar<T>(activity, undoCallback).show(t);

To handle -
    
    @Override
    public void onUndo(T t) {
    }
