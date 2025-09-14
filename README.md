## Cahier des charges
Ce projet consiste à **développer en Java une application permettant de simuler une équipe de robots pompiers évoluant de manière autonome dans un environnement naturel.** Pour ce faire, nous avons, dans un premier temps, *réalisé des simulations de difficulté croissante* : d’abord déplacer des robots de manière contrôlée sur la carte, puis être capable de trouver les plus courts chemins pour se rendre à un endroit donné. Dans un second temps, nous avons *organisé les déplacements et interventions des différents robots afin d’éteindre tous les incendies au plus vite.*

## Organisation du projet en paquetages

Nous avons organisé notre projet en paquetages dans le répertoire */src* de la façon suivante :
- **src/donnees** : La représentation des données du problème
- **src/evenements** : Contient les différentes classes de gestion d'événements tels que l’intervention sur un incendie, le
remplissage du réservoir, le déplacement d’un robot,...
- **src/exceptions** : On définit des classes d’exceptions dans ce répertoire. Il s'agit en
fait d'étendre la classe Exception pour chaque cas d'exception (Impossible de
déplacer un robot qui ne respecte pas les règles données dans le sujet) que peut
présenter le sujet.
- **src/io** : La classe LecteurDonnees.java.
- **src/simulations** : Tout ce qui concerne la simulation doit être géré dans ce
répertoire
- **src/tests** : Contient tous les fichiers tests des différents fonctionnalités demandées
dans le sujet


## Guide utilisateur

- **Exécution du programme**
  
Pour exécuter le programme, rendez-vous à la racine du programme et exécutez la commande suivante :
```sh make exeRunSimulation```
La simulation devrait alors s'ouvrir. 

Vous avez la possibilité de choisir la carte et la stratégie que vous souhaitez utiliser. Pour cela, vous pouvez
utiliser les ﬂags carte et strategie. La syntaxe est la suivante :

``` make exeRunSimulation [carte=<carte>] [strategie=<strategie>]``` 

où *[carte=<carte>] (facultatif)*: Nom de la carte à utiliser (nom du ﬁchier + extension, sans le
chemin). Par défaut, la carte utilisée est carteSujet.map.
*[strategie=<strategie>] (facultatif)*: Stratégie à utiliser. Les stratégies disponibles sont
elementaire et evolue. Par défaut, la stratégie utilisée est evolue.

Exemple :
``` make exeRunSimulation carte=carteSujet.map strategie=evolue```

- **Exécution de la simulation**

Les temps de déplacement, d'intervention et de remplissage étant assez longs (plusieurs secondes voire minutes), nous vous conseillons de modiﬁer les paramètres de la simulation pour l'accélérer. Les valeurs suivantes permettent d'avoir une vitesse de simulation agréable :
  *Tps entre 2 aﬃchages(ms)* : 1
  *Nb de pas simulés entre 2 aﬃchages* : 1
Vous pouvez dès lors lancer la simulation en appuyant sur le bouton **Lecture**. 
Le bouton **Suivant** permet d'avancer pas à pas dans la simulation.
Le bouton **Début** permet de revenir au début de la simulation.

- **Autres tests**

D'autres tests sont disponibles dans le dossier src/tests permettant de tester divers aspects du
programme. Il s'agit entre autres :

```make exeInvader``` : permet de lancer le jeu Invader

```make exeAffichageCarte``` : permet de s'assurer que le ﬁchier .map passé en
paramètre est bien sérialisé et aﬃché correctement sur la carte graphique.

```make exeLecture``` : Lit les données dans le ﬁchier carteSujet.map puis les aﬃche

```make exeLecteurDonneesKO``` : permet de s’assurer que des exceptions sont levées lorsqu’on tente
de lire des ﬁchiers .map mal construits (carteSujet_KO_*. map)

```make exeScenario0``` : teste le scénario KO décrit comme suit.
- Déplacer le 1er robot (drone) vers le nord, quatre fois de suite.
- Erreur : le robot est sorti de la carte.

```make exeScenario1``` : teste le scénario OK selon l'enchaînelent suivant. 
- Déplacer le 2ième robot (à roues) vers le nord, en (5,5). 
- Le faire intervenir sur la case où il se trouve. 
- Déplacer le robot deux fois vers l’ouest.
- Remplir le robot.
- Déplacer le robot deux fois vers l’est.
- Le faire intervenir sur la case où il se trouve.
- Le feu de la case en question doit alors être éteint..

```make exeDijkstra``` : Teste le plus court chemin vers une destination et le traduit en évènements de
déplacement à ajouter au simulateur

```make exeDijkstraFilter``` : Teste le calcul du chemin vers la case la plus proche remplissant la
fonction de ﬁltrage

```make exeRunSimulation``` : permet d’exécuter une stratégie sur une carte
