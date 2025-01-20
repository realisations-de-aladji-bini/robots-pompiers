# Cahier des charges
Ce projet consiste à **développer en Java une application permettant de simuler une équipe de robots pompiers évoluant de manière autonome dans un environnement naturel.** Pour ce faire, nous avons, dans un premier temps, *réalisé des simulations de difficulté croissante* : d’abord déplacer des robots de manière contrôlée sur la carte, puis être capable de trouver les plus courts chemins pour se rendre à un endroit donné. Dans un second temps, nous avons *organisé les déplacements et
interventions des différents robots afin d’éteindre tous les incendies au plus vite.* 
Plus précisement, nous avons commencé par implémenter toutes la représentation des données au travers de classes concernées telles que Robot, Case, Incendie, ainsi qu’un programme de test qui charge un fichier de données et affiche la carte correspondante, les robots et les incendies à l’aide de l’interface graphique fournie.
A ce stade, les données initiales sont simplement affichées et le simulateur ne fait rien en réponse aux évènements next() et restart() envoyés par l’interface graphique.
Nous avons par la suite ajouté un gestionnaire d’évènements au simulateur, et créer quelques fichiers de test (contenant une méthode main) permettant d’exécuter quelques scénarios (simples) prédéfinis. Les choses doivent commencer à bouger.
La suite a consister à mettre en place les classes (Path, Pathfinder, PathStep, Dijkstra) permettant de calculer le plus court chemin d’un robot vers une destination, et de le traduire en évènements de déplacement à ajouter au simulateur. Les collections Java sont les structures de données les plus utilisées.
Enfin, la toute dernière partie  consisté à créer un ensemble de classes permettant chacune de représenter un chef pompier de stratégie différente (élémentaire, évoluée...). Ces classes devront implanter a minima les mécanismes permettant de communiquer avec les robots pompiers de l’équipe (envoyer des ordres, recevoir des réponses), et les algorithmes spécifiés dans la description des stratégies ci-dessus.
L'objectif minimal pour ce projet est avant tout de mettre en œuvre une première stratégie de répartition des robots, même très simple. Si besoin, augmentez la taille des réservoirs des robots. Chose que nous avons faite avec brio. Nous sommes allé encore plus loin en implantant une stratégie plus évoluée où des données intermédiaires sont stockées par les différents objets, par exemple l’ensemble des cases contenant de l’eau, etc. Là encore, nous avons fait usage des collections
Java.

## Organisation du projet en paquetages

Nous avons organisé notre projet en paquetages dans le répertoire /src de la manière suivante :
- **src/donnees** : La représentation des données du problème
- **src/evenements** : Contient les différents tels que l’intervention sur un incendie, le
remplissage du réservoir, le déplacement d’un robot,...
- **src/exceptions** : On définit des classes d’exceptions dans ce répertoire. Il s'agit en
fait d'étendre la classe Exception pour chaque cas d'exception (Impossible de
déplacer un robot qui ne respecte pas les règles données dans le sujet) que peut
présenter le sujet.
- **src/io** : La classe LecteurDonnees.java fournie par les enseignants.
- **src/simulations** : Tout ce qui concerne la simulation doit être géré dans ce
répertoire
- **src/tests** : Contient tous les fichiers tests des différents fonctionnalités demandées
dans le sujet

# Conception et choix d'implémentation

- **Représentation des données**

Pour la représentation des données du système, nous avons, encore une fois, opté pour une répartition en paquetages ; chacun d’eux contenant les classes décrivant le nom du paquetage. Voici l'arborescence de ce paquetage.
Après la lecture de nos données  , nous avons fait le choix d'utiliser une classe correspondant à chaque objet de nos données. Ainsi, nous avons créé les classes Carte, Case, Incendie ainsi qu’une classe abstraite Robot avec des classes concrètes définissant chaque type de robot décrit dans le sujet. La classe Case représente une case avec les coordonnées et le type de terrain. Quant à la classe, elle représente l'ensemble des cases.
Enfin, notre classe Incendie est la représentation d’un incendie caractérisé par sa position et la quantité d'eau nécessaire pour l'éteindre. Dans un souci de coding style très propre et respectant les règles de l’art programmatique, nous avons centralisé nos données dans une classe DonneesSimulation regroupant les robots, les incendies ainsi que la carte qui les réunit.
Pour représenter les robots, nous avons défini une classe abstraite Robot dont les classes concrètes sont les différents types de robots : Drone, Robot à pattes, Robots à chenilles et les robots à roues. Nous avons utilisé le patron de conception Fabrique pour la création des robots. La classe abstraite Robot définit les méthodes identiques à tous les robots, alors que les classes concrètes héritières contiennent les méthodes qui leur sont propres et redéfinissent les méthodes abstraites de la classe mère Robot. Chaque robot possède ses propres propriétés telles que comme la vitesse, la position, la capacité du réservoir, ses temps
d’intervention et de remplissage de son réservoir. 

- **Le simulateur**

L'une des tâches principales du simulateur est de garder une liste d'évènements et de les appeler au bon moment. Ce dernier pouvant manipuler un grand nombre d'événements, il est important d'utiliser une structure de données pour ajouter des événements de manière ordonnée et récupérer le premier événement.
Pour implémenter cela, nous avons décidé d'utiliser une PriorityQueue (définie dans le package java.util). Ainsi, en utilisant la date de l'évènement comme priorité, nous pouvons ajouter des évènements en O(log(n)) (contre O(n) si on avait utilisé une LinkedList par exemple) tout en assurant l’ordre nécessaire à la bonne exécution des évènements. De même, pour retirer un évènement de la PriorityQueue, cela se fait aussi en O(log(n)) (puisqu'il suffit de récupérer le premier élément mais qu'il faut ensuite réordonner la heap), cette fois moins bien qu’une LinkedList qui le ferait en O(1). Pour pouvoir réinitialiser la simulation, il nous suffit de remettre les données des robots et incendies à leurs valeurs initiales.

- **Le plus court chemin**

La recherche des plus courts chemins est gérée par le paquetage pathfinding. On y définit la classe abstraite Pathfinder, un objet attaché à une carte et qui fournit une méthode pour calculer le plus court chemin vers un feu spécifique pour un robot donné. Toute cette fonctionnalité est isolée dans le but de pouvoir potentiellement à l’avenir partager des données et des calculs entre plusieurs appels (e.g. conserver un chemin qui risque d’être utilisé à nouveau). Pour effectivement l’implémenter, il suffit de dériver Pathfinder. Lapremière implémentation est basée sur l’algorithme de Dijkstra.

- **Résolution du problème** 

Pour permettre l'exécution de la simulation et la coordination des robots, nous avons utilisé une classe abstraite ChefPompier. Grâce à cette dernière, une stratégie peut être implémentée en l'héritant et en implémentant les diverses méthodes de la classe ChefPompier. Voici un diagramme de classe représentant l'implémentation actuelle du chef pompier et de deux stratégies. A noter que pour ce projet, les deux stratégies (élémentaires et évoluées) sont très similaires, ainsi plusieurs méthodes sont directement définies dans la classe mère et n'ont pas été ré-implémentées dans les classes filles. Idéalement, il faudrait que toutes les méthodes de la classe ChefPompier soient abstraites et que chacune des classes filles les override. Cependant, dans un souci de factorisation du code, cela n'a pas été nécessaire ici. Toutefois, pour ajouter une nouvelle stratégie (plus complexe), il faudrait sûrement rendre toutes les méthodes de la classe mère ChefPompier abstraite et factoriser le code d'une autre manière. Par exemple en introduisant une nouvelle classe dont hériterait ChefPompierElementaire et ChefPompierEvolue et qui implémenterait les méthodes communes à ces 2 classes. Le ChefPompier orchestre toute la simulation, c'est lui qui va créer tous les évènements nécessaires pour les robots au fur et à mesure de la simulation. Pour cela, le chef pompier créera de nouveaux évènements une fois que d'autres seront finis ou lorsqu'il sera notifié de la fin d'une action par un robot (ex : réservoir rempli ou destination atteinte). Par exemple, le chef pompier va programmer le déplacement d'un robot vers un incendie et l'en informer. Une fois arrivé, le robot va notifier le chef pompier qu'il a atteint sa destination.
Le chef pompier va alors vérifier si le robot est bien sur la case correspondant à l'incendie qui lui a été affecté et va faire intervenir le robot. Une fois le réservoir vide, le robot notifiera de nouveau le chef pompier pour le prévenir. Le chef pompier programmera une nouvelle séquence de déplacement vers l'eau la plus proche, puis créera l'évènement de remplissage, etc...
Certaines améliorations/optimisations de la stratégie évoluée ont été implémentées dans la stratégie élémentaire, d'où le fait que leur code soit si similaire. Les seules différences entre ces 2 stratégies sont l’automatisation de la prise de décision pour le chef pompier évolué (au lieu de tous les n pas de temps) et l’affectation des robots aux incendies.

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

```make exeLecture``` : Lire les données dans le ﬁchier carteSujet.map puis les aﬃche

```make exeLecteurDonneesKO``` : permet de s’assurer que des exceptions sont levées lorsqu’on tente
de lire des ﬁchiers .map mal construits (carteSujet_KO_*. map)

```make exeScenario0``` : teste le scénario KO décrit dans le sujet

```make exeScenario1``` : teste le scénario OK décrit dans le sujet

```make exeDijkstra``` : Teste le plus court chemin vers une destination et le traduit en évènements de
déplacement à ajouter au simulateur

```make exeDijkstraFilter``` : Teste le calcul du chemin vers la case la plus proche remplissant la
fonction de ﬁltrage

```make exeRunSimulation``` : permet d’exécuter une stratégie sur une carte
