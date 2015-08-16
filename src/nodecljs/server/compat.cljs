(ns server.compat
  (:require
   [cljs.nodejs :as nodejs]))

                                        ; Required for goog.net.XhrIo on node
                                        ; https://www.quora.com/Does-Node-js-utilize-XMLHttpRequest?share=1
                                        ; https://github.com/google/closure-library/wiki/Using-Closure-Library-with-node.js
                                        ; "any libraries in Closure Library that use the DOM will not work on NodeJS"
                                        ; https://github.com/google/closure-library/issues/439
                                        ; https://github.com/mbostock/d3/issues/1816
                                        ; http://stackoverflow.com/questions/8554745/implementing-an-ajax-call-in-clojurescript
                                        ; http://stackoverflow.com/questions/21839156/node-js-javascript-html-xmlhttprequest-cannot-load

(def xhr (nodejs/require "xmlhttprequest"))
(set! js/XMLHttpRequest (.-XMLHttpRequest xhr))
