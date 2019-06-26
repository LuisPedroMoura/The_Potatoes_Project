# The Potatoes Project
## A Dimentional Analysis Language


... READ ME WORK IN PROGRESS ...






The Potatoes Project is actually two projects in one. First a powerful Domain Specific Language for creating all kinds of Units that offers an interface that guarantees checked operations between them. Second, the Potatoes, a General purpose Language that uses the previous plataform offering a statically typed sintax for operating with the defined units that can be very  easily  and intuitively used even by non programmers.

### First, The 'Double Trouble'
Why isn't the type Double enough?
When dealing with units or quantities of a specific dimension in a General Purpose programming language there is an obvious limitation. There is no way to distinguish two variables aside from their names. And when operating with units, adding or multiplying yields different results, one maintains the dimension the other does not. And what if the SI units are not enough? And what if you need to create a new base unit and derived other units from it? And what about prefix multipliers?
The Double Touble DSL allows you to create all of this very easily. As a simple example, the code bellow creates 17 prefixed types/units with 169 different combinations to calculate area with equivalent (same dimension) units.
```
units {
  meter "m";
  yard "yd";
  fathom "ftm";
  area "m^2" : meter * meter;
  length [meter] : (1.09361) yard | (0.546807) fathom;
}
prefixes {
  Mega "M" : 10^6;
  kilo "k" : 10^3;
  pico "p" : 10^-12;
}
```
### The 'Potatoes Language'
...

"2 meter * 2 meter = 4? And what are those 4? Potatoes?", every physics teacher in Portugal

Potatoes Language Reference Manual:
Potatoes Language Tutorial:
