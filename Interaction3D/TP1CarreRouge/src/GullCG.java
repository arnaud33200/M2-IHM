/*
 *	@(#)GullCG.java 1.9 02/04/01 15:03:45
 *
 * Copyright (c) 1996-2002 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN
 * OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
 * LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed,licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

import javax.media.j3d.CompressedGeometry;
import javax.media.j3d.CompressedGeometryHeader;

public class GullCG extends CompressedGeometry {

    public GullCG() {
	super(cgHeader, cgData) ;
    }

    private static final byte cgData[] = {
          25,    0, -120,   16,  -58,  -52,   33,  -35,
        -128,   67,  -36,    0, -127,  -72,   97,   14,
          64,    2,   29,  104,    4,    2,  -45,    8,
         125,  -89,   16,   51, -116,   33,   -1, -104,
          71,  -64,   48, -106,   31,    1,   32,  -52,
           2, -127,    6,    5,    5,   32,   10,  -78,
        -128,   20,   14,    6,   40,   74, -128,   81,
         -43,   24,  -94,   44,    1,   86, -104,   98,
         -96,   49,    5,   34,  112,   10,  -68,  -29,
          21, -115,  -56,   40,    8,  -64,   81,  -74,
        -128,  -90,   13,   49,   71,   30,    2,  -81,
         -68,  -30,  -62,  122,   56,   37, -107,   60,
        -109,  120,    0,    0,   26,  -32,   18,   85,
        -128,   14,  -16,   71,  -13,   66,  -33,  103,
         127,  -81,   78,  -61, -106, -112,    0,    1,
         -32,   13,  -86,  -96,    4, -103,  -36,   59,
         -23,   25,   39,   18,   11,  122,  -80,    1,
         -34,  -22, -112, -109,  -80,  -71,   -7,  -39,
         -40,    0, -127,    0,    0,    0,    0,   43,
          97, -102,  118,   16,   83,  -32, -121,  -87,
         -32,  -86,   -4,   32,  121,    8,   33,  -48,
        -100,  -39,   64,    0,    0,   50,    0,  -47,
          -9,  -97, -118,   53,   75, -122,   89,  -54,
         -10,    8,    0,   69,  -98,    0,    0,   54,
           3,  -67,    0,   29,  -17,    2,   64,    0,
           5,  126,  -93,  -56,  -31,   15,   74,  -95,
        -128,    5,  -64,    0,    3,  -46,    8,    0,
          36,    0,    0,   58,   93,  -92,    0,    0,
          30,    0,   87,    4,    0,   23,    4,   68,
          36,   64,   30,  -17,  -25, -121,  -40,   22,
          13,   48,   78,  -43,   73,  -64,  -10,   48,
          51,  -25,   -1,  -82,  -65,    3,   55,  -71,
          41,  124,  -50, -118,  -64,    4, -102,  112,
           0,    1,  -20,    5,   98,   59,  -58,  -95,
         -87,   42,   -9, -112,  -99, -107,  -91,  -11,
         127,    5,   36,  -46, -112,  121,    9,  -39,
         -17,   -1,  -41,    0,    0,    0,  112,    4,
        -121,  -19, -124,  -16,  -44,   66,  -40,   36,
         116,  -83,  -96, -128,    7,  -37,  100,  -37,
         121,   11,  123,  -81, -106,   43,   82,   14,
         -85,   -8,   41,  -95,   67,   81,   18,  125,
          15,   39, -114,  -88,   63,  -10,    0,  -11,
           5,    4,   73,  -38,   13,   80,  -64,    7,
          64,   56,   14,  -56,  108,    3,  -64,   96,
           5,  -33,   81,   24,   11, -115,   26,  -95,
        -104,   95,  -69,  -95, -112,   15,    0, -128,
          40,   30,   67,  -27,    1,  107,  -55,  102,
         -39,   97,   67,  103,   66,   79,   67,   37,
         -60,   62,  -95,  -88,  -32,  105,   72,  106,
        -113,  -59, -108,   54,  -64,   43,   50,    9,
          86,   30,   45,   72,  101,   31,  -51, -120,
         106,   -8,   75,  -24,   98,  111, -106,  -12,
          54,  104,   54,   47,   87, -115,  -61, -106,
          64,  -47,   13,  -80,   10, -116, -122,   39,
          -3,  118,   -7,    0,    0,    0,    0,   42,
         -13, -118,   27,   96,    9,   27,  -20,   71,
          -8,  -96,  -50,   61,  111,   87,  102,  -83,
         -93,  -54,   47,  -37,   51, -113,   27,  122,
          98,  -52,  -91,  -75,    1,   53,  -96,    8,
          97, -120,   54,  -44,   55,   -1,  -10,  105,
        -108,  -84,   -4, -101,  -64,   67,   41,   -4,
         105,   67,  117,  -60,   92,   67,  115,   -4,
         -65,  124, -125,   90,   64,  118,  -85,   97,
          67,   39,  126,   96,   67,   78,   80,   65,
         -15,   12,  -57,   61,   94,   67,   50,    3,
         -93,   33, -107,   -2,  -44,   16,   91,  -34,
        -115,  -88,  109,   24,   75, -120,  109, -113,
        -108,   20,   49,   52,   59,  122,   25,   51,
         -29,   23,  -85,  -39,   33,  -50,  -28,  104,
        -122,  -16,    2,    0, -128,   90,   67,   20,
           1,  -93,  124, -122,   31,   91,  113,  -59,
          13,  -55,   -7,  121,   12,  -57,   10,  -10,
        -122,  120,    0,    1,   -8,    8,  -56,   33,
         -49,  -59,   94,  -78,  -45,  -85,    6,  -33,
         105,  -55, -121,  -77,   99, -113,   91,  105,
          -2,   77,  -37,   70, -118,   -2,   52,  102,
        -122,  -45, -120,  -66, -122,   25,   -7,   65,
          65,   19,   67,  -69,   33,  -92,    1,    0,
           0,   23,   16,  -37,   31,   40,  113,  -90,
         120,  118,  -84,  -48,  -55,  -17,  -37,   16,
         -59,   62,  -88,   28, -126,   21,   70,    1,
        -108,    3,   80,    1,  -96,   45,  107, -115,
         124,   58,  -60,  102, -128,    0,    7,   64,
          27,  -60,  125, -107,  127,   42,  -40,  -83,
          57,  -15,  -71,    1,  -32,  -50,  -60,   42,
         123,  106,   60,  -16,  -32,  -66,   85,   26,
         -14,  -31,   82,  -20,  -49, -128, -120,   94,
         -64,    0,  -25,   50,   85,   26,  -52,  -80,
          23,  -21,    7, -120,    1,  118,   53,  -55,
          75,  -83,  -96,    2,   24,  -16,    0,    0,
           0,    4,   18,  -77,   92, -101,  -64,    8,
          84,   72,  -22,  -33,   85, -126,  -42,  -69,
         -41,  -79,  -49,  -69,  126, -128,    5,    0,
          -1, -128,  125,   54,    0,  -53,   47,  110,
          91,  -59,   64,   13,  119,  115,  -59,   63,
          93,  -28,   -6,  -23,  -61, -128,    1,   40,
           0,    0,   80,   75,   64,  -18,  -33, -123,
          -2, -117,  -74, -116,   26, -106,  -80,    6,
          91,  -70,   21,  -23,  -44,    0, -121,   12,
           7,   64,    0,    5,   38,  -73,  118,  -79,
        -114,  104,  118,    0,   -5,   87,    2,   85,
         -46,   94,  115, -112, -117,   72,  124,  100,
         -32,  106,  106,   81,  -98, -121, -100,  116,
          76, -121,  -23, -112,   40,   43,   40,  -92,
          57,  -96,    4,  121,   47,  -29,  -65,   81,
         -26,    2, -128,   16,    6,   62,  -85,   86,
         -67,   -8,   74,  -35,  -77,  -62,   -5,  -81,
        -106,   56,    0,    0,   31,    1,  -91,  -48,
           0,   67,   27,  -62,  100,   43,   80,   68,
          61,  -48,    1,   24,    0,   45,    1,  -16,
           7,   18,    1,  -23,  -88,  -84,  127,   -6,
          11,   37,  -98,   14,   57,  107,  -61,  -52,
        -114, -128,    1,   91,  -92,  -95,  -82, -128,
          16,  -64,    1,  108,    0, -128,   55,  111,
        -101,   96,  -80,   61,  -90,  -94,  122,   49,
         -21,   14,   -2,  -73,   99,  115,   81,   11,
          90, -118,   74,  -99,   30,   -9,   -9,   67,
         -57,   80,   42,   79,  -14,    3,  -86,  -91,
         -94,   51,   -1,   83,   64, -118,   50,   53,
         -59,   74,  -89,  126,  -23,   32,   13,   57,
          40, -123,   76,   97,  -41,   91, -115,  -23,
         107,  -83,  -37,   81,   67,   27,  -73, -113,
          58,   22,  -80,    4,   88,   64,  109,  -10,
         -64,   14,  -80,  -19,  -43,   15,   62,  -80,
        -116,  -14, -121,  -11,  110,  -25,   38, -109,
           3,   55, -103,   85,  -65,  -79, -117,  -37,
          66, -102,   97,  -70, -123,   43,  104, -128,
          21,  100,   40,   71,  -55, -125,  104,   33,
         -43,   82,   84, -102,    7,   -3,  -32, -114,
          20,  -37,  -27,   -4, -107,  116,   67,    3,
          -7,  -91,   -9,  -99,  -25,  -44,  -71,  -57,
           4,    5,  -75,   74,    0,    1,  107, -120,
         -19,  -92,   21,  -66,  100,    8, -124,   -6,
         -79,   18,  -44,  -27,   12,   12, -112,  -32,
        -108,  -68,   99,  123,  -89,  -39,   98,  -53,
          51,  -10, -128,   10,   42,  -48,   -9, -108,
         -34, -100,   -7,  -52, -123,  -39,  100,  -46,
          85,   45,   16,    0,   12,   97,  -58,  -25,
          32,  111,  -32,    0,  -48,    0,  -48,   10,
         -73,   86,  -18,  104,  114, -102,  -80,   32,
           0,   88,    0,   64,    7,  -25, -127,   66,
          -6,  -75,   74,   73,   78,  -51, -125,    4,
        -103,  -88,    1,   78,  -62,   54,   84,   90,
         -14,   32,   35,  -18,  -40,  -67,  -44,  106,
        -102, -128,    2,  107,  -24, -124,  -64,   66,
         -51,   15,  100,  -87,   44,    0,   70,  125,
        -120,   95,   33,   50,  123,   70,   83,    0,
        -115,  122,   22, -102,   35,   92, -112,   94,
         -87, -121,  -66,  -64, -105,  -65,  -15,   17,
         -22,  122,  -80,   33,   94,   -5,    5,   75,
         -35,  114,   65,   99,   99,   94,   28,  -52,
          -3,  -77,   42,  -16,  -24,  -87,   84,   12,
         -76,   76,   90,  110, -120,    0,   86,  -36,
         -95,   85,    0, -122,  -62, -125,   97,   67,
          13,    0, -127,  -78,  -19,   63,   -3,  -46,
          80,    0,  -84,    0,   10, -128,   46,    3,
          40,    4,   55,  114,   -8,  -38,   16,  -59,
         -12,   84,  -90,  -35,  -83,  111,   64,   21,
        -128,    3,   96,   30,    3, -112,   12, -111,
        -120,   13,  -24,   14,  -32,    5,   29,   16,
           1,   12,  123,   -6,  -60,   86,   82,   25,
          59,  -56,   11,  -74,  -16,    4,    0, -104,
         -91,    0,   16,  -53,   59,   29,  101,   43,
         119,  -41,  -23, -100,    6,   74,   81,   56,
          10,   -6,   32,    1,   91,  -27,  -31, -124,
           5,  -40,  -22,  -90,   46,  -78, -128,    5,
          99,  -64,    0,    0,    0,   19, -128,   33,
        -106,    5,  -44,  -26,  -57,  -97, -113,  -64,
          20,  -93,   72,    2,   55,   48,    8,  111,
          80,   31,  -32,   31,  -16,   12,  -30,  -26,
         -88,    9,  -69, -128,    8,   98,    3,   64,
         120,    5,  -67,  118,  -12,   59, -118, -122,
        -115,  122,   -6, -122, -125,  -95,  -77,  -95,
        -105,   73,  -28,   -6, -122,  -86, -127,   51,
         -95, -105,  125,  -22,   -6, -122,  -38,  -31,
         -65,   84,  -86,  -14,   95,   71,  -97,   67,
         111,  -66,  -81,  -47, -124,   92,   22,    1,
         -43,   16,    1,   13,   97,  -18,  -63,  -82,
         -46, -120,  106, -110, -122,  -17,  120,   97,
         -87,   82,   67,  -99,   75,  -48,  -33,   95,
          79,    2,  -32,  -32, -104,   13,   41,    0,
           8,  103,   79, -105, -115,  112,  112,   70,
           3,   21,   84,   51, -101,  -53,   18,    8,
          14,  -62,  -83,   72,  100,   72,   72,  -24,
         102,   71,  -73, -117,   17,   30,  -65,  -32,
         -40,   36,    4,   46,  -51,   76,   44,   96,
         -44,    0,  -57,  102,   54,  -31,   -2,   80,
           0,  -71,   98,  -72,   80,   46,   82,  -64,
           8,   96,  -80,    0,   32,    0,   16,    8,
          55,  106,  -98, -121,  -19,   64,   11, -108,
          36,  103,    8,  -88,    8,    0,   46, -116,
        -101,  104, -128,   50,  -54,  112,   93,   73,
          64,    0,  115, -128,    0,    3,  -32,   14,
        -100,   25,   96,    0,  -66,    0,    0,   22,
          49,  -64,   12,  -76, -124,   35,  -95,   56,
           1,  -10,    0,   11,  -32,  122,    1,   98,
          20,  114,  -38,   16,  -18, -124,  -32,    7,
         -40,    0,   47, -128,    0,    5,  104,   97,
         -53,    8,  127,  -70,   35, -128,   29,  -32,
           0,  -66,    0,    0,   20,    7,  -17,  104,
         124,  -70,   47,  -34,    0,   11,  -32,    0,
           1,   93,  124,  -78, -121,  -29,  -93,   56,
           0,   22,    0,   11,  -32,    0,    1,   82,
         -58,  122,  -38,   31,  110, -101,  125, -128,
           2,   -8,    0,    0,   86,  -26,   28,  -74,
        -121,  -29,  -92,   56,    1, -106,    0,   11,
         -32,    0,    1,   -2, -104,    0,   35,   53,
        -127,   65,  -82,   34,   38,  -51,  -74,  -34,
           0,  -71,  123,   66,  111,  -63,  -24,   16,
           1,   12,    0,   17,    0,    0,   21,    5,
         -48, -125,  109,   48,    7, -118, -125,   83,
         -68,   10,  115, -112,    1,    0, -128,   18,
         -47,  -89,   61,  121,  -86,   77,   58,   16,
         109,   92,   65,   86,  127,  -82, -100,   31,
          96,    0,   64,    0,    0,   30,   -7,   68,
        -116,   32,    0,    3,  -32,   28,   72,   13,
        -110, -125,   43,    0,   17,  107,  -64,   23,
         -92,    0,   46,  -64,    1,  -96,   13,    1,
          91, -112,    1,   12,    5,   -2,  -66, -122,
        -110, -104,   81,   19,   30,   -2,   60,    2,
          18,   96,   48, -112,   20,   51,   16,   38,
         -51,  126,   67,   56,   33,  -82,  -78,  -87,
        -117,  -10,   10,  -52,  -86,  -18,  -97,  120,
          62,  -47,   67,   -9,  -86,   81,  -17,    0,
           8,    0,   24,    5, -127,   -5,  -38,    0,
          47,   67,   -9, -128,    4,    0,    0,    2,
         -80,   -1,   97,    7,   -9,  -82,   81,  -53,
           0,    8,    0,    0,    5,    4,  112,    3,
         -19,  -96,  -14,  -11,  -56,   57,   96,    1,
           0,    0,    0,  -82, -116,    0,  125, -108,
          31,   30,  -71,   71,  -68,    0,   32,    0,
           0,   21,    7,  -53,  104,   61,  -67,   35,
           0,    5,   96,    1,    0,    0,    0,  -84,
           3,   45,  -96,   -8,  -11,   12,    0,   21,
        -128,    4,    0,    0,    3,   -8,   15,  -77,
         -64, -108,   16,  -30,   14,   61,  105,  -40,
         -81,   27,  124,  -75,   64,   40,    6,  -11,
         -54,   32,  -32,    1,    0,    0,    0, -111,
         -63, -106, -116, -125,  115,    5,    0,   33,
          76,    0,    1,  -64,   61,  -96,   25,   26,
        -116,   24,   37,   66, -113,  -55,   42,   77,
          19,  -35,   -4,  -73, -111, -110,  -14,   12,
         -23,   12,    0,  101,  -91,    0,    0,    1,
           0,  104,   70,    0,   43,  -39,  -99,  -46,
          78,   45,   -5,   58,  -30,    2, -127,   27,
          44,  -22,   80, -118,   53,  -24,   58,   -3,
         -17,  -76,  -90,   95,   18,   55,  -16,  -39,
          -4, -121,  -55,  -25,  -11,  -38,   85,   68,
          60,  -76,   15,   -5,  124,   40,    0,   86,
         -14,   12,   -2,    3,  124,  -16,   63,    0,
         -69,  -91,  102,    2, -128,   16,    6,   48,
          31,   43,   94,   -4,   50,   42,   14,   90,
        -126,  -95,  107,  -18,  -96,    0, -102,   71,
         -11,  -68,   16,  -37,   11,  -59,  122,   -2,
          64,    1,  -64,   59,    1,  -38,   24,   80,
         -48,    5,   64,    0,   15,  -46,  104,  -82,
         -57,  -19,   -4, -115,  115,  112,   92,   20,
         -28,   -3, -127,   64,  125,  -64,  127,  -64,
          56, -125, -113,   74,  118,   43,   78,  -35,
         107,   47,  -18,   91, -121, -111,    0,    0,
           0,  126, -128,   63,  -23,   31,  102,  -65,
          32,   53,  -30, -104,  102,   86,  -16,  -16,
           8,   86,    2,  -20,   31,   24,  -11,  -56,
           1,   78,    0,   16, -127, -104,   10,   18,
         102,  -51,    8,  122,    0,   86,    0,   19,
           1, -120,   10,  -32,   34,  -55,   65,  111,
          64,   16,  119, -128,    5, -128,   98,    3,
         -82, -103,  100,  -64,    0,    7,  -80,   31,
        -111, -128,   11, -108,   89,   16,  106, -107,
         -56,    1,  118,  -75,  -10,  121,  -92,    0,
         -69,  121,    5,   32,  -79,   43, -104,    2,
         -20,  123,   -2,  -51,   72,    1,  118,   -6,
          40,   -5, -105,   48,    2,   24,   56,    0,
           4,    0,    4,    2,  -15,   90,   90,   93,
          -4,    1, -117,   86,  -87,  -89,   67,    0,
          86,  -98,  -94,  121,    1,  -66,  -34,    0,
         -64,   18,   10,   86,  -40,   21,  115,    0,
          70,  -28,    1,  -22,    1,  -14,    0, -126,
          33,   12,  -87,   38,   -3,  -30, -104,  -28,
         124,   89,  -63,  -58,   77,  -76,  -79,  -89,
          98,    8,  -64,    7,    0,   56,   11,  -62,
          59,  -45,   18,   46,  -62,  -51, -128,   14,
           0,   96,   25, -128,  -33,   93,   64,   45,
         -35, -101,  120,   31,    0,  -96,   58, -128,
        -122, -120,    7,    0,    0,    1,  -39,   12,
           0,  124,    2,    0,  -80,   70,  -36, -127,
         119,   16, -122,  -16,   62,    0, -128,  126,
         -19,   54,   70,  121,  108,   26,   17, -117,
         107,  -52,  -30,   35,  -36,   19,   93, -128,
          28,  102,  -39, -122,  -61,  118,    1,   12,
           0,   28,   10, -128,  -13,  114,  -33,   14,
          66, -109,    7,   48,    2,   24,  -24,    3,
         -52,  -92,  110,  123,  124,  -96,    8,   67,
          40,    8,  -87, -111, -117,  -71,  -98, -124,
          73, -128,    3,   96,   30,    2,  105,    8,
          32,   64,  -74,   41,   48,   -6,  -67, -105,
         -56,   43,  106,  -53,  -75,    0,   67,  113,
           2,  121,  -89,  114,    4, -128,   57,  -60,
          80,  -50,   56, -107,   16,  -46,  -64,   45,
          72,   96,   88,  107,    8,  105,  -64,   55,
         -31, -112,  108,  -89,  -76,  -59,    0,  107,
        -124,   42,   26,  -53,  -21, -116,   92,   14,
        -100, -107, -109,  -62,   94,   64,   33,  -75,
        -128, -121,  -12, -102, -122,   17, -119,  101,
          67,   88,   27,  -27,   41,   13,  -76,   22,
         -20, -126,    0,    0,    0,    0,   71,   67,
         113,  -66,   51,  -95,  -92, -122,   12,  -94,
        -126,   33,  -13,  103,   35,   46,   -1,  -47,
          40,   68, -101,   48,   41, -100, -119,  -79,
          47,   60,   83,   64,   67,  101, -122,   51,
        -103, -119,  107,  -62, -102,    1,   12,    8,
          22,   -2,   42,  -73,   77,    6,   91,   32,
           2,   49,  -73,  -89,  -63,    8,   98,  -45,
         -21,  -16,   10,   50,   41,  -87,   74,  -76,
          53,   91,   75,  -43,  -40,    0,   42,    1,
         -96,   36,  -46,    0,  111,  -85,   57,   94,
          93,  119, -104,  116,   17, -104,  -65,   84,
          97,  100, -102,   37,   15,  -68,  126,   73,
          47, -111,  115,  -53,  -46,  -72,    0,  102,
         -60, -123,  119,    1,  -54,   64,    4,    2,
           0,  118,   71,   92,    0,   12,   12, -128,
         -34,  -55,  -65,   74,  -85, -111,   78, -125,
          -3,  127, -100, -111,  -80, -128,   10, -108,
         -56,   16,    1, -109,  -24,   21,  -44, -115,
          -9,  -32,  -16,   35,   37,   14,  -22,  -69,
        -106,  -15,   64,    3,   45,  -22,   33,   86,
         -64,   32,  -41,   32,  117,   52,   62,  -27,
        -101, -118,  -74, -125,  -66,   56,  -13,   93,
         113,  -56,   21,   79,  -76, -128,    0, -103,
         122,  126,  -48,   20,   93,  -90,   47,   42,
         -40,    8,   17,  -83,  -94,  -33,    8,   93,
        -113,   10,   79,   62, -128,   23,  109,  -25,
          -8,    1,   33,   90,    0,   33, -118,    5,
         -44,  -54,  -64,  127, -113,   64,   16,  -64,
           2,   80,   15,    1,   -5,  -27,  -38,  -16,
         -83,   41, -118,   15,  -37,  -57,  -79,  -94,
         -51,   50,  -59,  -47,   20,  -59,   64,   15,
         -75,  107,  -53,  -91,  -96,   -9, -128,    1,
          48,    0,    0,   80,   94,  -19,   92,  -10,
        -127,  -20,    0,   14,   82,  125,   83,  -24,
        -121,   56, -124,   61,  -67,  -88,  -73,   64,
         120,    0,    0,   84,    0,  -61,  -19,   99,
          32,  -33,  -26,  -76,  -26,  -17,  -51, -122,
          38, -111,  -84,  -36,   12,   93,  -77,  -66,
         -85,  -86,    0,  107,  -68,    6,  -36,   92,
         -74,   30,  102,   71,   64,    3,   93,   78,
         -80,  -75,   -7,    5,   93,   -6,  -57,  -74,
         -79,   79, -109,   78,   22,  117,  108, -111,
         122,   52,   96,  -49,   38, -117, -125,  123,
          10,  114,  121,   85,   96,  -48,    0,  -96,
          31,   16,   13,   39,  -42,  113,   50, -109,
          -9,   90,   59,   93,   15,   -8,  -79,  116,
         -14,   77, -128,   29,  104,  -19,  -42,  -97,
         -94,  -62,    6,  -33,   44,    0,   -5,  108,
          -3,   91,  -19,    7,  -83,  -27,   81,   49,
          24,  -12, -109,  123,  -51,   60,   39,  111,
         -50,   23,  -78,   53,  -10,   14,  -80,  -93,
         122,  -87,   33,  -78,  -30,   59,  -95,  -96,
          10, -128,    0,   30,   61,   92, -112,  -53,
         -25,  104,  -76,   24,  -79,    9,   37,   32,
          22,    4,  -57,    1,  109,   65,    7,   26,
         -98,   59,  106, -101,  103,  -64, -116,  -52,
          11,   29,   23,  106,  107,  106,  -51,  -15,
         120,   -5,  -20,    1,   54,  -16,  -47,  -77,
           0,   38,  -81,  -54,   18,   -8,   32, -122,
          69,   70,  117,   23,   44,    0,    0,   30,
           0,  -68,    0,  109, -128,  -45,  -37,   80,
          48,    5,  118,  -25,  -85,   50,  110,  108,
        -111,   51,  -47,   23,   16,  -96,   45,   99,
         -51, -110,  -12,   96,  -63,  -42,   20,  120,
          89,   86,   41,  120,   32,    5,   41,   -5,
           0,   13,  -99,  104, -100, -128,   10,  -80,
          16,    0,  104,   15, -104,    5,  106,  -85,
         125,    9,   57,   -1,   91,  -16,    0,   44,
           7,  -32,    3,  -10,   55,   40,   -1,   80,
          65, -126,  -64,   66,  -20,  -37,   66,  -59,
          95,   64,    4,   55,  -20,   26,   13,  -37,
        -126,  125, -106,  -48,    1,   13,  -48,  -65,
         -90, -126,  -79, -123,   21,   32,    8,  108,
          95,  -91, -111,  115,   44, -101, -112,    1,
          82,   16,    0,  103,    1,  -51,    0, -112,
         -14,  103, -112, -122,   98,   46,  -43,  -71,
          41,  126,  -76,    0,  -71,   79,    2,  127,
         108,   -2, -128,   23,  109,   93, -111,  -74,
          90,    0,   33,  -96,   63,  -94, -107,  -86,
          70,   92,  -64,   21,  -67,   64,    0, -128,
         127,  -64,   51,    0,   23,  126,  -40,   69,
          68,    0, -101,   71,   -5,  -84, -127,  -82,
         104, -123,   85,   80,  -55,  -81,   47,  -88,
         109,  -46,   27,  -11,   74,  -83,   49,  -35,
         116,  113,  112,    0,   86,    0,  -72,   15,
          32,    4,  -37,  124,   11,  126, -116,  -32,
          11, -113,  126,   40,  -92,    0,   33, -110,
          -2,   87,   53,  -51,   64,  -81,  -51,   74,
         -36,  121,   -4,  102,  -15,  117,  -19,    4,
          93,  -32,    4,   49,    0,   96,   60,    3,
         -23,   26,   19, -124,  -92,   33,   13,  -94,
         -16,   -6, -122,  -83, -127,   54,   33, -118,
         -99,  -79,   32, -107,  112,   30,   -8, -122,
        -125,  -66,  -77,  -95,  -91,  122,   16, -114,
        -122,   92,  123,  113,   65,   69,  125,  125,
          67,   66,   48,  -43,   80,  -53,   78,  -49,
          29,   37,   10,  121,  -59,   36,  -61, -114,
        -109,  -28,  103,  -92,    0,   46,  -57, -115,
          72,  127,  -12,    0,  -55,  101,  -48, -119,
        -117,   22,  -64,    1,   25, -110,   21,  -60,
          66,   26,   43,  -21,  -59,  -55,  -62,  -58,
         -21,   46,  -96, -128,   25,  101,    0,    0,
           1,  -64,   31,    9,   64,    1,  -75,  -73,
         -55,   82,    0,  -89,   94,   13,  -76,  -56,
          74,   -1,   83,  115,   72,  -21, -104,   43,
          56,  -75,  -32,  -10,   74,  -11,  -66,  126,
          11,   72,  113,  -40,   29,  -51,  -12, -127,
           6,  -85, -116,  -85,   78,   30,  -65,  -44,
        -111,   -1,  -95, -111,   62,  -33,   68,  -55,
         -29,  -91,  112,  -93,   37,    1,   72, -111,
        -114,    0,  125,  -96,  -32,  -38,    9,   70,
          76, -102,    5,  -28, -116, -109,  -53,  -58,
          -7,  -37,   -7,    0,   10,  114,  100, -105,
         -47,  -53,   59,   46,  -37,  -23,   64,    2,
         -73,  112,  103,  -16,    4,  102,  -18,  104,
          21,  -40,   70,  -70,    0,   88,    0,    1,
        -113, -123,   88,  -89,  -55, -112,  -84,  -45,
        -107,  -82,   28, -128,   40,  105,   24,   55,
          61,  -73,  -40,   15,  -72,    0,    8,    4,
          47,  -27,   88,  -18,   -3,   74,   50,  104,
        -111,   31,  -27,   -4, -115,  115,  111,  -92,
          22,  -12,  -10, -128,    3, -128,   10,    3,
          78,  -56,   97,  127,  104,    8,  105,   24,
          53,   -2,  -43,  119,  -21,  -12,  -13,  104,
         -95,   71,  -19,   -4,   13,  -13,  -57,  -20,
          16,  -16,  -36,  -84,   95,   88,   52,   -5,
           8,    1,   78,   97,  -68,  -67,  120,  -40,
           0,   66,    1,  -96,   40,   64,    2,  -20,
          -2,  -57,  -88,    2,  -84,    0,   38,    0,
         -16,   21,   -2,   60, -102,  -38,  -12, -115,
          88,    0,   88,    1,  -32,   43,   95,   57,
          -9, -115,   32,   -3,  113,  -28,  -45,   31,
         -62,   57,    7,   -5,  -30, -105,  106,  102,
          59,  -44,   32,    7,   56,    0,   52,    6,
          96,   61,    3,   54,  -24,    0,    1,  -56,
          11,    0,   92,  -70,   40,   85,  113,    0,
          16,  -42,  -79,   73,  -73,  106,   66,   21,
         104,   64,   11,  -79,  -20,   11,   14,   32,
           1,   88,  119,  -60,   14,   96,   16,  -34,
          64,    0,   32,   63,  -32,   23,  -48,  -35,
          82,  -20,  -92,  110, -102,  -15,   52,    8,
          67,   24,   17,   83,   35,    2,   58,  -84,
        -120,  -41,   50,  -55,  -65,  123,   39,  -42,
           6,  -71,   71,   19,    1,  126,   60,    2,
           4,   98,   87,  124,   19,   60, -112,  -41,
         111,   63,  -96,    9, -119, -109,   47,   24,
         113, -108, -126,   55,    2,  -66,   88,  -60,
         120, -127,  -24,   18,  -64,   78,   40,   99,
          32,  -21,   56,   -5,  -66,  100,  -80,   19,
        -116,  -37,   77,  -75,  -18,  -64,   33, -128,
           3, -128,  -80,   25,   -4,   71, -122,   61,
         103,   39,   20,   54,  -81,  -54,  -99, -112,
         -64,    7,    0,  -56,   11,  -63,  106,  -71,
         -25,  100, -103,   51,    0,   28,    3,   64,
          51,    0,  -33,   90,  -44,   89,  -39, -101,
           8,   31,    3,   96,   44,    0, -122,  101,
           9,  114,   67,    0,    0,   -1,  -64,   -1,
           0,   88,   35,   82,   67,  -71,    8,   43,
           0,   64,   -1,  -64,   -1, -128,  126,    1,
        -110,  101,   95,   -6,  -37,  113,    0,    9,
         -79,   19,  -26,  112,   41,  -51,  -83,   18,
         113, -108,   52, -113,  -25,  -44,   54,   33,
          13,  -59,   13,    5,  -11,   97,   12,  -58,
           8,  -50, -122,   90,  -17, -102,  -46,   26,
         -82,    9,  -80, -116,   83,  -23,  -56,   69,
          73,   84,    0,  -28,    7,  -12,    3,   47,
          17,  -68,   43,  -26,  -62,   32,   92,   25,
         -15,  -43,   24,    1,   13,  -98,  -18,  -64,
        -122,  -51,  120,   98, -102,   24,   71,  -35,
        -127,    5,   90,   48,  -42,  -92,   49,  -36,
          36,  116,   52,  -29,  -21,  -11,   72,  -59,
          21,  -22,  106,  -14,  -73,  -16,   36,   16,
          19,  105,  110, -105,  -16,   41,   71,   54,
          80,  -38,  109,   73, -110,  -63,   21,   72,
          35,   38,   59,   49,    8, -109,   24,   88,
          29,   72, -125,   50,   91,  -93,   16,    9,
         -77,  -53,  126,   20,  -64,   33,    2,   58,
         -90,    8, -109,  102,   61,   41, -112,   70,
          65,   -9,   75,   17,   38,   22,  -12,   83,
          35,  125,   -4,  -14,  -62,  -95, -106, -103,
         -66,  -82,   70,  -60,  -16,  115,   17,   30,
          -4,    0,   11,    1,   -8,    0, -126, -120,
          75,   65,  -47,  -42,  -58,  126,  -55, -128,
           0,   15,   96,   52,  115,   14,  -37,   79,
         -70,   71,   10,   60, -102,   74,   34,  124,
         -25,   10,    5,  112,  -24,  -46,  -68,  105,
          23,  -34,   -3,    4,   95,  -20,   69,  -37,
          50,  -98,    8,   17, -113,   97,   20,   72,
          33, -107,   88,  -45,  -56,  -49,  -80,  -85,
         -30,   36,   85,  -77,    9,   68,   33,  -94,
         -67,  -97,   72,  -52,   35, -108,  -95,   23,
          96,   40,   22,   40,   64,   12,   87, -122,
          -9,   27,  -23,    2,    8,  -73,   -6,  -26,
        -112,   22,    4,  -44,  116,   85,    2, -116,
        -109,  -24, -101,  -34,   56,    1,  -74,   16,
           0,    1,  -16,   14,   61,   89,   56,   51,
         -21, -107, -101,  -72,  -17,   64,   46,  -64,
           2,  -80,   13,    1,  -37,   16,    1,    4,
           0,    0,    0,    0,  -63,  118,  101, -103,
          91,  -60,   32,    4,  -40,   52,   58,  119,
          64,   32,    0,  -52,    0,    0,    0, -120
    } ;

    private static final CompressedGeometryHeader cgHeader ;

    static {
	cgHeader = new CompressedGeometryHeader() ;
	cgHeader.majorVersionNumber = 1 ;
	cgHeader.minorVersionNumber = 0 ;
	cgHeader.minorMinorVersionNumber = 1 ;
	cgHeader.bufferType = CompressedGeometryHeader.TRIANGLE_BUFFER ;
	cgHeader.bufferDataPresent = CompressedGeometryHeader.NORMAL_IN_BUFFER ;
	cgHeader.start = 0 ;
	cgHeader.size = cgData.length ;
    }
}
