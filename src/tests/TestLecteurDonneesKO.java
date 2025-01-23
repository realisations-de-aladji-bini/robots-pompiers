/*
 * TestLecteurDonneesKO.java

 */

package tests;

import io.LecteurDonnees;
import exceptions.NatureInconnue;
import exceptions.RobotInconnu;

/**
 * Fichier de test qui permet de s'assurer que des exceptions sont bien levées
 * lorsque l'on tente de lire des fichiers .map mal construits (fichiers
 * carteSujet_KO_*.map)
 */
public class TestLecteurDonneesKO {
    public static void main(String[] args) throws Exception {
        boolean exceptionLevee = false;

        try {
            LecteurDonnees.creeDonnees("cartes/carte_KO_1.map");
        } catch (NatureInconnue e) {
            exceptionLevee = true;
        } catch (Exception e) {
            throw new Exception(
                    "Le fichier Carte_KO_1.map aurait dû lever une exception NatureInconnue et pas "
                            + e.getClass().getName());
        }

        if (!exceptionLevee) {
            throw new Exception(
                    "Le fichier Carte_KO_1.map aurait dû lever une exception NatureInconnue, mais aucune n'a été levée");
        }

        exceptionLevee = false;
        try {
            LecteurDonnees.creeDonnees("cartes/carte_KO_2.map");
        } catch (NatureInconnue e) {
            exceptionLevee = true;
        } catch (Exception e) {
            throw new Exception(
                    "Le fichier Carte_KO_2.map aurait dû lever une exception NatureInconnue et pas "
                            + e.getClass().getName());
        }

        if (!exceptionLevee) {
            throw new Exception(
                    "Le fichier Carte_KO_2.map aurait dû lever une exception NatureInconnue, mais aucune n'a été levée");
        }

        exceptionLevee = false;
        try {
            LecteurDonnees.creeDonnees("cartes/carte_KO_3.map");
        } catch (NatureInconnue e) {
            exceptionLevee = true;
        } catch (Exception e) {
            throw new Exception(
                    "Le fichier Carte_KO_3.map aurait dû lever une exception NatureInconnue et pas "
                            + e.getClass().getName());
        }

        if (!exceptionLevee) {
            throw new Exception(
                    "Le fichier Carte_KO_3.map aurait dû lever une exception NatureInconnue, mais aucune n'a été levée");
        }

        exceptionLevee = false;
        try {
            LecteurDonnees.creeDonnees("cartes/carte_KO_4.map");
        } catch (IllegalArgumentException e) {
            exceptionLevee = true;
        } catch (Exception e) {
            throw new Exception(
                    "Le fichier Carte_KO_4.map aurait dû lever une exception IllegalArgumentException et pas "
                            + e.getClass().getName());
        }

        if (!exceptionLevee) {
            throw new Exception(
                    "Le fichier Carte_KO_4.map aurait dû lever une exception IllegalArgumentException, mais aucune n'a été levée");
        }

        exceptionLevee = false;
        try {
            LecteurDonnees.creeDonnees("cartes/carte_KO_5.map");
        } catch (IllegalArgumentException e) {
            exceptionLevee = true;
        } catch (Exception e) {
            throw new Exception(
                    "Le fichier Carte_KO_5.map aurait dû lever une exception IllegalArgumentException et pas "
                            + e.getClass().getName());
        }

        if (!exceptionLevee) {
            throw new Exception(
                    "Le fichier Carte_KO_5.map aurait dû lever une exception IllegalArgumentException, mais aucune n'a été levée");
        }

        exceptionLevee = false;
        try {
            LecteurDonnees.creeDonnees("cartes/carte_KO_6.map");
        } catch (RobotInconnu e) {
            exceptionLevee = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception(
                    "Le fichier Carte_KO_6.map aurait dû lever une exception RobotInconnu et pas "
                            + e.getClass().getName());
        }

        if (!exceptionLevee) {
            throw new Exception(
                    "Le fichier Carte_KO_6.map aurait dû lever une exception RobotInconnu, mais aucune n'a été levée");
        }

        System.out.println("Tous les tests ont été exécutés avec succès");
    }

    private static void verifieLectureKO(String fichierDonnees, Exception[] exceptionsAttendues) throws Exception {
        // TODO: Améliorer les appels de la fonction main en utilisant cette fonction
        // qui permettrait de tester automatiquement un nom de fichier contre une liste
        // d'exceptions attendues. Mais j'ai pas encore trouvé de moyen d'appeler cette
        // fonction, quelque chose comme ça: verifieLectureKO("cartes/carte_KO_1.map",
        // new Exception[] { DataFormatException.class });

        try {
            LecteurDonnees.creeDonnees(fichierDonnees);
        } catch (Exception e) {
            for (Exception exceptionAttendue : exceptionsAttendues) {
                if (e.getClass().equals(exceptionAttendue.getClass())) {
                    return;
                }
            }

            throw new Exception(
                    "Exception non attendue: " + e.getClass().getName() + "pour le fichier " + fichierDonnees);
        }

        throw new Exception("Aucune exception n'a été levée pour le fichier " + fichierDonnees);
    }
}