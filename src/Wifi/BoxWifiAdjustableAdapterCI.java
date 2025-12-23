package Wifi;

import fr.sorbonne_u.components.hem2025.bases.AdjustableCI;

/*
* Interface qui adapte BoxWifiExternalControlCI vers AdjustableCI
* 
* <p><b>RAISON D'ÊTRE : Résoudre une incompatibilité fondamentale d'architecture</b></p>
* 
* <p>Cette interface a été créée pour résoudre un problème critique d'intégration entre
* le framework de gestion d'énergie (HEM) et notre équipement BoxWifi. Sans elle,
* le système ne pouvait pas fonctionner car :</p>
* 
* <ul>
*   <li><b>Problème initial :</b> Le HEM utilise exclusivement l'interface standard AdjustableCI
*       pour contrôler tous les appareils modulables, mais notre BoxWifi expose une interface
*       spécifique BoxWifiExternalControlCI avec des méthodes métier concrètes.</li>
*   <li><b>Incompatibilité :</b> Aucune relation d'héritage ou d'implémentation directe
*       n'existe entre AdjustableCI et BoxWifiExternalControlCI, rendant impossible
*       la connexion directe entre le HEM et la BoxWifi.</li>
*   <li><b>Échec technique :</b> Lors des premiers tests, nous obtenions des NullPointerException
*       dans AbstractConnector.connect() car le connecteur ne trouvait pas les méthodes
*       AdjustableCI attendues sur l'interface BoxWifiExternalControlCI.</li>
* </ul>
* 
* <p><b>SOLUTION APPORTÉE :</b> Cette interface agit comme un adapteur qui :</p>
* 
* <ul>
*   <li><b>Combine les deux contrats</b> en une seule interface cohérente</li>
*   <li><b>Permet au connecteur WifiConnector</b> d'implémenter la logique de transformation
*       entre les opérations génériques (upMode, downMode) et les actions spécifiques
*       (turnOn, activateWifi, etc.)</li>
*   <li><b>Garantit la compatibilité</b> avec le framework HEM tout en préservant
*       les fonctionnalités métier de la BoxWifi</li>
* </ul>
* 
* <p><b>RÉSULTAT :</b> Grâce à cette interface, le HEM peut maintenant :</p>
* 
* <ul>
*   <li>Contrôler la BoxWifi via le standard AdjustableCI</li>
*   <li>Exécuter tous les tests automatiques de gestion d'énergie</li>
*   <li>Bénéficier des fonctionnalités avancées spécifiques à la BoxWifi</li>
* </ul>
* 
 */
public interface BoxWifiAdjustableAdapterCI 
    extends  BoxWifiExternalControlCI {
    // Cette interface combine les deux contrats
    // Les méthodes de AdjustableCI seront implémentées dans le connecteur
    // qui traduira les appels génériques en opérations spécifiques BoxWifi
}