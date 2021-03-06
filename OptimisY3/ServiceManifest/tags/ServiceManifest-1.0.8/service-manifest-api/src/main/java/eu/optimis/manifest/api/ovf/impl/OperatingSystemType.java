/* 
 * Copyright (c) 2012, Fraunhofer-Gesellschaft
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * (1) Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the disclaimer at the end.
 *     Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 * 
 * (2) Neither the name of Fraunhofer nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 * 
 * DISCLAIMER
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 */
package eu.optimis.manifest.api.ovf.impl;

import java.text.MessageFormat;

/**
 * @author arumpl
 */
@SuppressWarnings( "javadoc" )
public enum OperatingSystemType
{

    // CHECKSTYLE:OFF - self explaining enumeration
    Unknown( 0 ),
    Other( 1 ),
    MACOS( 2 ),
    ATTUNIX( 3 ),
    DGUX( 4 ),
    DECNT( 5 ),
    Tru64_UNIX( 6 ),
    OpenVMS( 7 ),
    HPUX( 8 ),
    AIX( 9 ),
    MVS( 10 ),
    OS400( 11 ),
    OS_2( 12 ),
    JavaVM( 13 ),
    MSDOS( 14 ),
    WIN3x( 15 ),
    WIN95( 16 ),
    WIN98( 17 ),
    WINNT( 18 ),
    WINCE( 19 ),
    NCR3000( 20 ),
    NetWare( 21 ),
    OSF( 22 ),
    DC_OS( 23 ),
    ReliantUNIX( 24 ),
    SCO_UnixWare( 25 ),
    SCO_OpenServer( 26 ),
    Sequent( 27 ),
    IRIX( 28 ),
    Solaris( 29 ),
    SunOS( 30 ),
    U6000( 31 ),
    ASERIES( 32 ),
    HP_NonStop_OS( 33 ),
    HP_NonStop_OSS( 34 ),
    BS2000( 35 ),
    LINUX( 36 ),
    Lynx( 37 ),
    XENIX( 38 ),
    VM( 39 ),
    Interactive_UNIX( 40 ),
    BSDUNIX( 41 ),
    FreeBSD( 42 ),
    NetBSD( 43 ),
    GNU_Hurd( 44 ),
    OS9( 45 ),
    MACH_Kernel( 46 ),
    Inferno( 47 ),
    QNX( 48 ),
    EPOC( 49 ),
    IxWorks( 50 ),
    VxWorks( 51 ),
    MiNT( 52 ),
    BeOS( 53 ),
    HP_MPE( 54 ),
    NextStep( 55 ),
    PalmPilot( 56 ),
    Rhapsody( 57 ),
    Windows_2000( 58 ),
    Dedicated( 59 ),
    OS_390( 60 ),
    VSE( 61 ),
    TPF( 62 ),
    Windows_R_Me( 63 ),
    Caldera_Open_UNIX( 64 ),
    OpenBSD( 65 ),
    Not_Applicable( 66 ),
    WindowsXP( 67 ),
    zOS( 68 ),
    Microsoft_Windows_Server_2003( 69 ),
    Microsoft_Windows_Server_2003_64Bit( 70 ),
    Windows_XP_64Bit( 71 ),
    Windows_XP_Embedded( 72 ),
    Windows_Vista( 73 ),
    Windows_Vista_64Bit( 74 ),
    Windows_Embedded_for_Point_of_Service( 75 ),
    Microsoft_Windows_Server_2008( 76 ),
    Microsoft_Windows_Server_2008_64Bit( 77 );
    // CHECKSTYLE:ON

    private final int number;

    OperatingSystemType( int number )
    {
        this.number = number;
    }

    public int number()
    {
        return number;
    }

    public static OperatingSystemType findByNumber( Integer number )
    {
        if ( number != null )
        {
            for ( OperatingSystemType os : OperatingSystemType.values() )
            {
                if ( os.number == number )
                {
                    return os;
                }
            }
        }
        String message = "There is no operating system with number ''{0}'' specified.";
        throw new IllegalArgumentException( MessageFormat.format( message, number ) );
    }

}
