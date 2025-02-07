These are my notes!

Oak was Created, Java Came from Oak. HotJava Browser Created. Netscap proceeded to support Java. oracle acquired Sun Microsystems, owner of Java.
Java:
Built in Garbage Collection -> self cleaning memory
References instead of pointers -> Safer and easyier than worrying about pointers.
Classes dynamically linked at runtime
Java is hybrid, compiled/interpreted language



Java Code:
Specifically an Object Oriented language
/** is javadoc which is a tool that allows you to export a javadoc file automatically
Strings Are ALWAYS immutable!

Which is more efficient: string Builder or printf string? String Concatination i.e.  ("asd" + "asd") = "asdasd" is least efficient

TryWithResources is a very powerful method which allows you to auto close in file io by using try(openfile;){code using example;}   File will then auto close. ------- usefull!

Collections cannot store primatives. To store those, you will need to store them as objects. i.e. integer class and long class.

DeepCopy vs ShallowCopy: Shallow copy is  a copy of references. e.x. copy references of a linked list will miss the first, which will point a reference to the original item.

Cloneable to make a copy of an object. Cloneable is an interface so to use we do: public class MyClass implements Cloneable{}
	if that is not working, we will have to iterate ofer the list to make a deep copy of the list.




JSON:
[...] is an array
{...} is an object




Chess Project:
We need to build an interface to code the pieces -> most efficient and somewhat easier for future 


Look into Records lecture to avoid toString() and @Override methods

use Cloneable to check if king is in checkmate, take all of your moves, apply to a cloneable if move gets you out of check, return false i.e. your not in checkmate.
