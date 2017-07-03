(ns clojure-talk.core
  (:require [clojure.data.json :as json]))


;; strings
"Clojure rocks"

(class "Clojure rocks")

;; numbers
42
0.01

;; symbols
'dwayne

;; keywords
:age

;; precise arithmetic
(/ 1 3)


;; collections
{:firstname "Ellen" :lastname "Ripley"}
#{"a" "set" "of" "words"}

;; functions
(defn greeter [name] (println "Greetings," name))
(greeter "Boromir")


;; nested collections
(def aliens-characters [{:firstname "Ellen"
                         :lastname "Ripley"
                         :rank "Officer"
                         :guns [{:type :flamethrower :model "M240A1"}
                                {:type :pulse-rifle :model "M41A"}]}
                        {:firstname "Mark"
                         :lastname "Drake"
                         :rank "Private First Class"
                         :guns [{:type :smartgun :model "M56"}
                                {:type :knife :length 10}]}])

(get-in aliens-characters [1 :guns 0 :type])

(def aliens-character-from-json (json/read-json (slurp "resources/aliens.json")))

(map :guns aliens-character-from-json)

(defn has-weapon [weapon character]
  (let [guns (:guns character)]
    (some #(= (:type %) weapon) guns)))

(has-weapon "knife" (second aliens-character-from-json))

(->>
  (filter (partial has-weapon "knife") aliens-character-from-json)
  (map :lastname))



;; clojure.spec (generate fictional characters)
; guns : smartguns, pulse rifle, flamethrower, knife, shotgun, grenade, powerloader
; lastnames: Ferro, Frost, Apone, Hicks, Hudson...
; rank: sergeant, private, commander, lieutenant, civilian
; civilian = no gun


;; figwheel ?


