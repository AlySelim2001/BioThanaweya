// Vitest setup file for IndexedDB support via fake-indexeddb
// This ensures IndexedDB is available in both local and GitHub Actions environments

// Import fake-indexeddb/auto to polyfill IndexedDB
import 'fake-indexeddb/auto';

// Type declaration to ensure TypeScript knows about indexedDB
declare global {
  interface Window {
    indexedDB: any;
    IDBDatabase: any;
    IDBTransaction: any;
    IDBObjectStore: any;
    IDBIndex: any;
  }
}

export {};