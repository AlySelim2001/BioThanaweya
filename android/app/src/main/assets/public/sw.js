/**
 * Copyright 2018 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// If the loader is already loaded, just stop.
if (!self.define) {
  let registry = {};

  // Used for `eval` and `importScripts` where we can't get script URL by other means.
  // In both cases, it's safe to use a global var because those functions are synchronous.
  let nextDefineUri;

  const singleRequire = (uri, parentUri) => {
    uri = new URL(uri + ".js", parentUri).href;
    return registry[uri] || (
      
        new Promise(resolve => {
          if ("document" in self) {
            const script = document.createElement("script");
            script.src = uri;
            script.onload = resolve;
            document.head.appendChild(script);
          } else {
            nextDefineUri = uri;
            importScripts(uri);
            resolve();
          }
        })
      
      .then(() => {
        let promise = registry[uri];
        if (!promise) {
          throw new Error(`Module ${uri} didn’t register its module`);
        }
        return promise;
      })
    );
  };

  self.define = (depsNames, factory) => {
    const uri = nextDefineUri || ("document" in self ? document.currentScript.src : "") || location.href;
    if (registry[uri]) {
      // Module is already loading or loaded.
      return;
    }
    let exports = {};
    const require = depUri => singleRequire(depUri, uri);
    const specialDeps = {
      module: { uri },
      exports,
      require
    };
    registry[uri] = Promise.all(depsNames.map(
      depName => specialDeps[depName] || require(depName)
    )).then(deps => {
      factory(...deps);
      return exports;
    });
  };
}
define(['./workbox-7e5eb42b'], (function (workbox) { 'use strict';

  self.skipWaiting();
  workbox.clientsClaim();
  /**
   * The precacheAndRoute() method efficiently caches and responds to
   * requests for URLs in the manifest.
   * See https://goo.gl/S9QRab
   */
  workbox.precacheAndRoute([{
    "url": "registerSW.js",
    "revision": "1872c500de691dce40960bb85481de07"
  }, {
    "url": "pwa-maskable-512x512.png",
    "revision": "42905da47b9f7c49a0f04400a552ad34"
  }, {
    "url": "pwa-512x512.png",
    "revision": "42905da47b9f7c49a0f04400a552ad34"
  }, {
    "url": "pwa-192x192.png",
    "revision": "5b23ad0e293f990158500db970074338"
  }, {
    "url": "index.html",
    "revision": "a0e85aa8b54cfdf869caff9feefa1f97"
  }, {
    "url": "icon.svg",
    "revision": "53e4a7a405f431434012faeaefbf69c2"
  }, {
    "url": "ic_biothanaweya_art.jpg",
    "revision": "e80953a3369412cae749dc8a3417e052"
  }, {
    "url": "favicon.svg",
    "revision": "b34e31e1cdfc049b18d210ea31878d5f"
  }, {
    "url": "apple-touch-icon.png",
    "revision": "c5e5f72c5c836f5c4fce1ac7bebb5435"
  }, {
    "url": "assets/vendor-utils-oQXWb4Lk.js",
    "revision": null
  }, {
    "url": "assets/vendor-react-D1Gm9cjr.js",
    "revision": null
  }, {
    "url": "assets/vendor-motion-CUjqKZgJ.js",
    "revision": null
  }, {
    "url": "assets/vendor-core-1ssyoFBN.js",
    "revision": null
  }, {
    "url": "assets/index-u3RjyOhz.js",
    "revision": null
  }, {
    "url": "assets/index-D7OKZN17.css",
    "revision": null
  }, {
    "url": "apple-touch-icon.png",
    "revision": "c5e5f72c5c836f5c4fce1ac7bebb5435"
  }, {
    "url": "icon.svg",
    "revision": "53e4a7a405f431434012faeaefbf69c2"
  }, {
    "url": "pwa-192x192.png",
    "revision": "5b23ad0e293f990158500db970074338"
  }, {
    "url": "pwa-512x512.png",
    "revision": "42905da47b9f7c49a0f04400a552ad34"
  }, {
    "url": "pwa-maskable-512x512.png",
    "revision": "42905da47b9f7c49a0f04400a552ad34"
  }, {
    "url": "manifest.webmanifest",
    "revision": "424f65583db2e2234aaf781d82540f3d"
  }], {});
  workbox.cleanupOutdatedCaches();
  workbox.registerRoute(new workbox.NavigationRoute(workbox.createHandlerBoundToURL("/index.html")));

}));
