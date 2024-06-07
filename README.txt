Folders:
1) backend: Πρώτο πραδοτέο
2) gui: δεύτερο παραδοτεό
3) test: το backend, σε κάποια σημεία διαφοροποιημένο, για λόγους συμβατότητας με
	 το Android application.

How to Run:
1)"Τρέξτε" το app του test folder, σε κάποιο editor πχ IntelliJ
2)Στην συνέχεια μπορείτε να τρέξετε στο android studio το "MyApplication"
3)ΣΗΜΑΝΤΙΚΌ: στο ClientThread.java του ΜyApplication 
	     (MyApplication.app.src.main.java.com.example.myapplication.backend.ClientThread.java)
	     στην γραμμή 45 στην εντολή: 
		requestSocket = new Socket("localhost", 9090);
 	     αντί για localhost, εισάγεται την IPV4 της συσκευής που τρέχει τον Master.java
	     (ακόμα και εάν τρέχει στο ίδιο μηχάνημα).
