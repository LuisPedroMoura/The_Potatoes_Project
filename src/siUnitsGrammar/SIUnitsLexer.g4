lexer grammar SIUnitsLexer;

@header{
	package projeto.si_units_grammar;
}


MILIMETER: 'mm' ; // AMBIGUITY with meter and mili???

/**
 * International System of Units - PREFIXES
 * Prefixes are added to unit names to produce multiples and sub-multiples
 * of the original unit. All of these are integer powers of ten, and above
 * a hundred or below a hundredth all are integer powers of a thousand
 */

DECA:	'da';	// 10^1
HECTO:	'h';	// 10^2
KILO:	'k';	// 10^3
MEGA:	'M';	// 10^6
GIGA:	'G';	// 10^9
TERA:	'T';	// 10^12
PETA:	'P';	// 10^15
EXA:	'E';	// 10^18
ZETTA:	'Z';	// 10^21
YOTTA:	'Y';	// 10^24

DECI:	'd';	// 10^-1
CENTI:	'c';	// 10^-2
MILI:	'm';	// 10^-3
MICRO:	'u';	// 10^-6
NANO:	'n';	// 10^-9
PICO:	'p';	// 10^-12
FEMTO:	'f';	// 10^-15
ATTO:	'a';	// 10^-18
ZEPTO:	'z';	// 10^-21
YOCTO:	'y';	// 10^-24

MULTIPLE		: DECA | HECTO | KILO | MEGA  | GIGA
				| TERA | PETA  | EXA  | ZETTA | YOTTA
				;
SUBMULTIPLE		: DECI | CENTI | MILI | MICRO | NANO
				| PICO | FEMTO | ATTO | ZEPTO | YOCTO
				;

PREFIX			: MULTIPLE | SUBMULTIPLE ;


/**
 * International System of Units - BASE UNITS
 * The SI base units are the building blocks of the system and all the other
 * units are derived from them
 */
METER:		'm'  ; 	// Length
KILOGRAM: 	'kg' ; 	// Mass
SECOND: 	's'  ; 	// Time
AMPERE: 	'A'  ; 	// Electric current
KELVIN: 	'K'  ; 	// Thermodynamic temperature
MOLE: 		'mol'; 	// Amount of substance
CANDELA: 	'cd' ; 	// Luminous Intensity

// Radians and steradians have been categorized as derived units in 1995,
// but for calculation purposes will be treated as base units
RADIAN: 	'rad';	// Angle -> m/m
STERADIAN: 	'sr' ; 	// Solid Angle -> m^2/m^2

BASE_UNIT	: METER | KILOGRAM | SECOND | AMPERE | KELVIN
			| MOLE  | CANDELA  | RADIAN | STERADIAN
			;


/**
 * International System of Units - DERIVED UNITS
 * The derived units in the SI are formed by powers,
 * products or quotients of the base units and are unlimited in number.
 * Not all derived units are named (i.e. velocity (m/s))
 * The dimensions of derived units can be expressed in terms of the dimensions
 * of the base units.
 */
// Derived Units		QUANTITY					In other units	In Base Units
HERTZ: 			'Hz' ; 	// Frequency				1/s
NEWTON: 	  	'N'  ; 	// Force, Weight			(kg m)/s^2
PASCAL: 		'Pa' ; 	// Pressure, stress			N/m^2			kg/(s^2 m)
JOULE: 			'J'  ; 	// Energy, work, heat		N m	OR Pa m^3	(kg m^2)/s^2
WATT: 			'W'  ; 	// Power, radiant flux		J/s				(kg m^2)/s^3
COULOMB: 		'C'  ; 	// Electric Charge			A s
VOLT:			'V'  ;	// Electrical Potential		W/A				(kg m^2)/(A s^3)
FARAD:			'F'	 ;	// Capacitance				C/V				(s^4 A^2)/(kg m^2)
OHM:	'Ohm' | 'O'	 ;	// Resistance, impedance	V/A				(kg m^2)/(s^3 A^2)
SIEMENS:		'S'  ;	// Electrical conductance	1/O				(s^3 A^2)/(kg m^2)
WEBER:			'Wb' ;	// Magnetic flux			V s				(kg m^2)/(A^1s^2)
TESLA:			'T'  ;	// Magnetic flux density	Wb/m^2			kg/(A s^2)
HENRY:			'H'  ;	// Inductance				Wb/A			(kg m^2)/(s^2 A^2)
CELSIUS:  'Celsius'	 ;	// Temperature				273.15 K		K
LUMEN:			'lm' ;	// Luminous Flux			cd sr			cd
LUX:			'lx' ;	// Iluminance				lm/m^2			cd/m^2
BECQUEREL:		'Bq' ;	// Radioactivity							1/s
GRAY:			'Gy' ;	// Absorbed dose			J/kg			m^2/s^2
SIEVERT:		'Sv' ;	// Equivalent dose			J/kg			m^2/s^2
KATAL:			'kat';	// Catalytic activity						mol/s

DERIVED_UNIT	: HERTZ   | NEWTON	  | PASCAL | JOULE   | WATT
				| COULOMB | VOLT	  | FARAD  | OHM     | SIEMENS
				| WEBER   | TESLA	  | HENRY  | CELSIUS | LUMEN
				| LUX 	  | BECQUEREL | GRAY   | SIEVERT | KATAL
				;



/**
 * International System of Units - Lexicographic Conventions
 * NOTA: de momento esta informacao esta aqui para que todo o grupo esteja
 * sintonizado no que respeita as regras oficiais de representacao dos
 * numeros e unidades
 * - The value of a quantity is written as a number followed by a space 
 * (representing a multiplication sign) and a unit symbol
 * - Symbols are mathematical entities, not abbreviations, and as such do not
 * have an appended period/full stop (.)
 * - A prefix is part of the unit, and its symbol is prepended to the unit
 * symbol without a separator
 * - Compound prefixes are not allowed.
 * - Symbols for derived units formed by multiplication are joined with a
 * centre dot (nao consigo usar o simbolo :| ) or a non-breaking space; e.g.,
 * N(ponto)m or N m.
 * - Symbols for derived units formed by division are joined with a solidus (/),
 * or given as a negative exponent. E.g., the "metre per second" can be written
 * m/s, m s^-1
 * - The first letter of symbols for units derived from the name of a person is
 * written in upper case; otherwise, they are written in lower case.
 * - Symbols of units do not have a plural form; e.g., 25 kg, not 25 kgs.
 * - Spaces should be used as a thousands separator (1000000) in contrast to
 * commas or periods (1,000,000 or 1.000.000)
 * - Any line-break inside a number, inside a compound unit, or between number
 * and unit should be avoided.
 * - Since the value of "billion" and "trillion" can vary from language to
 * language, the dimensionless terms "ppb" (parts per billion) and "ppt"
 * (parts per trillion) should be avoided. No alternative is suggested in
 * the SI Brochure.
 */
LEFT_PAR: '(';
RIGHT_PAR: ')' ;
SOLIDUS: '/' ;
SPACE: ' ' ;
POWER: '^' INT ;
//DIMENTION:	PREFIX? (BASE_UNIT | DERIVED_UNIT) ;
INT: '0' | [1-9][0-9]* ;
NUMBER:	('0' | INT)('.'[0-9]+)? ;
ERROR: . ;
