(ns t3tr0s.core
  (:require
    [clojure.browser.repl :as repl]
    [clojure.string :refer [join]]
    [jayq.core :refer [$ ajax document-ready]]))

;;------------------------------------------------------------
;; Connect to the Browser REPL
;;------------------------------------------------------------

(defn connect-repl []
  (ajax {:url "repl-url"
         :cache false
         :dataType "text"
         :success #(repl/connect %)}))

(enable-console-print!)

(def pieces
  {:I [[0,0],[0,-1],[0,1],[0,2]]
   :L [[0,0],[0,1],[1,1],[0,-1]]
   :J [[0,0],[0,-1],[0,1],[-1,1]]
   :S [[0,0],[-1,0],[0,-1],[1,-1]]
   :Z [[0,0],[-1,-1],[0,-1],[1,0]]
   :O [[0,0],[-1,0],[-1,1],[0,1]]
   :T [[0,0],[-1,0],[1,0],[0,1]]})

(def empty-board [[0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]
                  [0,0,0,0,0,0,0,0,0,0]])

(def state (atom {:board empty-board}))

(defn get-cell-str
  "Return a space or asterisk for the given cell."
  [cell]
  (if (= 0 cell) "_" "*"))

(defn row-str
  "Create a string from a board row."
  [row]
  (join "" (map get-cell-str row)))

(defn board-str
  "Create a string from the tetris board."
  [board]
  (join "\n" (map row-str board)))

(defn print-board
  "Prints the board to screen."
  []
  (println (board-str (:board @state))))

(defn write-to-board!
  "Writes a given value to the x,y position on the board."
  [ x y value ]
  (if (and (<= 0 x 9) (<= 0 y 21))
    (swap! state assoc-in [:board y x] value)))

(defn write-coord-to-board!
  [[cx cy] x y]
    (write-to-board! (+ cx x) (+ cy y) 1))

(defn write-piece-to-board!
  "Writes a given piece to the board."
  [piece x y]
  (doall (map #(write-coord-to-board! % x y) piece)))

(defn clear-board!
  "Clears the board."
  []
  (swap! state assoc :board empty-board))

(defn rotate-piece
  "Create a new piece by rotating the given piece clockwise."
  [piece]
  (doall (map (fn [[x y]] [(- y) x]) piece)))

(defn test-rotate-piece!
  "Clear the board, write the piece at 5, 9, rotate the piece, write the piece at 5, 1,
    print the board."
  [piece-key]
  (clear-board!)
  (write-piece-to-board! (piece-key pieces) 5 9)
  (write-piece-to-board! (rotate-piece (piece-key pieces)) 5 1)
  (print-board))

(defn test-piece!
  "Clear the board, write a piece at 5,9, and print it."
  [piece-key]
  (clear-board!)
  (write-piece-to-board! (piece-key pieces) 5 9)
  (print-board))

(defn init []
  (connect-repl)
  (print-board))

(document-ready init)