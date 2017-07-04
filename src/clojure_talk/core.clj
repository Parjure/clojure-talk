(ns clojure-talk.core
  (:require [clojure.data.json :as json]
            [clojure.spec.alpha :as spec]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.alpha :as s]))


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
                         :lastname  "Ripley"
                         :rank      "Officer"
                         :guns      [{:type :flamethrower :model "M240A1"}
                                     {:type :pulse-rifle :model "M41A"}]}
                        {:firstname "Mark"
                         :lastname  "Drake"
                         :rank      "Private First Class"
                         :guns      [{:type :smartgun :model "M56"}
                                     {:type :knife :length 10}]}])

(get-in aliens-characters [1 :guns 0 :type])

(def aliens-character-from-json (json/read-json (slurp "resources/aliens.json")))

(map :guns aliens-character-from-json)

(defn has-weapon
  [weapon character]
  (let [guns (:guns character)]
    (some #(= (:type %) weapon) guns)))

(has-weapon "knife" (second aliens-character-from-json))

(->> aliens-character-from-json
     (filter (partial has-weapon "knife"))
     (map :lastname))



;; clojure.spec (generate fictional characters)
; guns : smartguns, pulse rifle, flamethrower, knife, shotgun, grenade, powerloader
; lastnames: Ferro, Frost, Apone, Hicks, Hudson...
; rank: sergeant, private, commander, lieutenant, civilian
; civilian = no gun

(spec/def :crew/gun (spec/keys :req-un [:gun/type
                                        :gun/model
                                        :gun/length]))


(spec/def :crew/firstname string?)
(spec/def :crew/lastname string?)
(spec/def :crew/type #{:soldier :civilian})
(spec/def :crew/rank #{"private" "lieutenant" "sergeant" "commander"})
(spec/def :gun/type #{:flamethrower :powerloader :smartguns :pulse :knife :grenade :rifle :shotgun})
(spec/def :gun/model (s/and string? #(< 10 (count %) 15)))
(spec/def :gun/length (spec/int-in 10 200))
(spec/def :crew/guns (spec/coll-of :crew/gun :min-count 1 :max-count 3 :distinct true))

(spec/def :crew/member (spec/keys :req-un [:crew/firstname
                                           :crew/lastname
                                           :crew/type]))

(spec/def :crew/soldier (spec/merge :crew/member
                                    (spec/keys :req-un [:crew/rank
                                             :crew/guns])))

(defmulti crew-type :type)


(defmethod crew-type :civilian [_]
  :crew/member)

(defmethod crew-type :soldier [_]
           (spec/merge :crew/member :crew/soldier))

(s/def :crew/person (s/multi-spec crew-type :type))


(gen/sample (spec/gen :crew/person) 25)


(s/fdef has-weapon
        :args (s/cat :gun :gun/type :person :crew/soldier)
        :ret boolean?)

(s/exercise-fn `has-weapon 25)

;; figwheel ?


