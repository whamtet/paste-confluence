(ns paste-confluence.clipboard
  (:import [java.awt.datatransfer DataFlavor StringSelection Transferable]))

(defn clipboard []
  (.getSystemClipboard (java.awt.Toolkit/getDefaultToolkit)))

(defn slurp-clipboard []
  (.getTransferData (.getContents (clipboard) nil) (DataFlavor/allHtmlFlavor)))

(def html-flavors
  (into #{}
        (map #(DataFlavor. %))
        ["text/html;class=java.lang.String"
         "text/html;class=java.io.Reader"
         "text/html;charset=unicode;class=java.io.InputStream"]))

(defrecord HtmlSelection [html]
  Transferable
  (isDataFlavorSupported [_ flavor]
    (contains? html-flavors flavor))
  (getTransferDataFlavors [_]
    (into-array DataFlavor html-flavors))
  (getTransferData [_ flavor]
    (condp = (.getRepresentationClass flavor)
      java.lang.String html
      java.io.Reader (java.io.StringReader. html)
      java.io.InputStream (java.io.StringBufferInputStream html))))

(defn spit-clipboard [html]
  (let [selection (HtmlSelection. html)]
    (.setContents (clipboard) selection nil)))

