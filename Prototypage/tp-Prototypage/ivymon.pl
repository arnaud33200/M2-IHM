#!/usr/bin/perl

#=================================================================================
#
# Tk::LabFrame Extension
#
#=================================================================================
# derived from Tk::LabFrame v3.021
# 
package Tk::eLabFrame;


use Tk;
require Tk::Frame;

use strict;
use base qw(Tk::Frame);
Construct Tk::Widget 'eLabFrame';


sub Populate {
   my ($cw, $args) = @_;
   my $f;
   my $label;
   my $button;
   my $lside = exists $args->{-labelside} ?
   delete $args->{-labelside} : 'top';
   my $ltext = delete $args->{-label};
   my $lbutton = delete $args->{-buttonbmp};
   $cw->SUPER::Populate($args);
   if ($lside =~ /acrosstop/) {
      my $border = $cw->Frame(-relief => 'groove', -bd => 2);
      $cw->Advertise('border' => $border);
      my $pad = $border->Frame;
      $f = $border->Frame;
      $label = $cw->Label(-text => $ltext);
      $button = $cw->Button(-image => $lbutton) if defined $lbutton;
      my $y = int($label->winfo('reqheight')) / 2;
      my $ph = $y - int($border->cget(-bd));
      if ($ph < 0) {
         $ph = 0;
      }
      $label->form(-top => 0, -left => 4, -padx => 6, -pady => 2);
      $button->form(-top => 0, -right => -4, -padx => 6, -pady => 0) 
         if defined $button;
      # $label->place('-y' => 2, '-x' => 10);
      $border->form(-top => $y, -bottom => -1, -left => 0, -right => -1,
                     -padx => 2, -pady => 2);
      $pad->form(-left => 0, -right => -1, -top => 0, -bottom => $ph);
      $f->form(-top => $pad, -bottom => -1, -left => 0, -right => -1);
      # $cw->Delegates('pack' => $cw);
   } else {
      $f = $cw->Frame(-relief => 'groove', -bd => 2, %{$args});
      $label = $cw->Label(-text => $ltext);
      $label->pack(-side => $lside);
      $f->pack(-side => $lside, -fill => 'both', -expand => 1);
   }
   $cw->Advertise('frame' => $f);
   $cw->Advertise('label' => $label);
   $cw->Advertise('button' => $button) if defined $button;
   $cw->Delegates(DEFAULT => $f);
   $cw->ConfigSpecs(-labelside => ['PASSIVE', 'labelSide', 
                                   'LabelSide', 'acrosstop'],
                                   'DEFAULT' => [$f]);
}


#=================================================================================
#
# M A I N
#
#=================================================================================

package main;

$SIG{INT} = \&quit;
$SIG{QUIT} = \&quit;
use Tk;
use Tk::LabFrame;
use Tk::Font;
use Tk::Balloon;
use Tk::FBox;
use Tk::ErrorDialog;
use Tk::Dialog;
use Tk::ProgressBar;
use Sys::Hostname;
use Time::HiRes qw(gettimeofday);
use Ivy;
use Carp;
use strict;
use Getopt::Long;
use Tk::CmdLine;
use vars qw/$VERSION $opt_help $opt_b $opt_bus $opt_history @opt_bind 
            @opt_send $opt_size $opt_undersize $opt_out $opt_debug/;
# **** VERSION ****
$VERSION = '1.21';

# options initialisation
# geometry
my $minW = 1024;
my $minH = 768;
my $smallsized = 0;
my $coef = 1;

# my $ivy_port; # undef:=> default value is treated by ivy-perl
my $ivy_port = "127.255.255.255:666"; # undef:=> default value is treated by ivy-perl
my $history = 200000;
my $casesensitiveflag = 1;
my $regexpflag = 0;
# misc
my $enlarge = 0;
my $hostname = hostname();
my %clientsMessagesTpl;
my %connectedClients;
my %clientsBindings;
my %clientBindingsTpl;
my %bindings;
my %effectivebindings;
my %bindingsIndex;
my $selectedClient;
my %msgToSend;
my %msgToSendIndex;
my $msgToSendFlag = 0;
my $searchIndex;
my $searchString;

my %searchHistory;
my @searchHistory;
my $searchHistoryIndex = -1;

my %sendHistory;
my @sendHistory;
my $sendHistoryIndex = -1;

my %bindHistory;
my @bindHistory;
my $bindHistoryIndex = -1;

my $clockbackwardflag = 0;
my @packinfo = ();
my @messagesbuffer;

my $markers_cnt = 0;

# displayed messages counters
my $messagesNumber = 0;
my $messagesLastNumber = 0;
my $messagesSpeed = 0;
my $recordedNumber = 0;
my $deletedNumber = 0;
my $bufNumber = 0;

my $bindingsFlag = 0;
my $stopFlag = 0;
my $noMessageYet = 1;
my $jump_cnt = 0;

my $outputfile;

my $noUpdateFlag = 0;
my $loadedFileFlag = 0;
my $loadingFlag = 0;

my $appname = 'IvyMon';
$appname =~ s/ /_/g;

# messages array
my @send_def; 

# available (not effective) bindings array
my @bind_def;

# Effective bindings array
my @effectivebind;


#----------------------------------------------------------------------------------
# command line options management
#----------------------------------------------------------------------------------

Tk::CmdLine::SetArguments(-font => '7x14');
Tk::CmdLine::SetArguments();

if (not GetOptions('-help', '-history=s', '-b=s', '-bus=s', '-bind=s@',
         '-send=s@', '-size=s', '-undersize', '-debug', 
         '-out=s') or $opt_help) {
    print "\n";
    print "IvyMon version $VERSION\n";
    print "\n";
    print "Usage: ivymon [-b[us] bus] [-help] [-history length]\n";
    print "              [-size size] [-undersize] [-debug]\n";
    print "              [-bind regexp1] ... [-bind regexpN] \n";
    print "              [-send message1] ... [-send messageN] \n";
    print "              [-out outputfile] [standard X11 options...]\n";
    print "              [inputfile]\n";
    print "\n";
    print "Options :\n";
    print "     -b <[addr]:port>    Ivy bus (\$IVYBUS by default)\n";
    print "     -history <length>   Messages list history length\n";
    print "                         ($history messages by default)\n";
    print "     -size <VGA|SVGA|XGA|SXGA|UXGA>    Size of the main window\n";
    print "     -undersize          If set, slightly reduces the main window\n";
    print "     -bind <regexp>      Ivy binding regular expression\n";
    print "                         ((.*) by default)\n";
    print "     -send <string>      Ivy message to send\n";
    print "     -out <file>         output filename\n";
    print "\n";
    
    exit ;
}
if ($opt_bus) {
    $ivy_port = $opt_bus;
} elsif ($opt_b) {
    $ivy_port = $opt_b;
}
$history = $opt_history if $opt_history;
my $title;
my $ivy_version = $Ivy::VERSION;
my $ivy_cvsrevision = 0;
my $ivy_versionstring = "v$ivy_version";
if ($ivy_version =~ s/Revision: (.*)//) {
    $ivy_version = $1;
    $ivy_cvsrevision = 1;
    $ivy_versionstring = "v$ivy_version (cvs revision)";
}
if ($ivy_port) {
    $title = "Ivymon v$VERSION ($ivy_port) - Ivy $ivy_versionstring";
} else {
    $title = "Ivymon v$VERSION (default port) - Ivy $ivy_versionstring";
}
   
push(@effectivebind, @opt_bind);
@effectivebind = ('(.*)') unless @effectivebind > 0;
push(@bind_def, @opt_bind);
push(@send_def, @opt_send);

#===================================
#
#   Size options
$opt_size = '' unless defined $opt_size;
if($opt_size eq "UXGA" || $opt_size eq "1600") {
       $minW = 1600;
   $minH = 1200;
   $smallsized = 0;
   $coef = 1.2; 
   Tk::CmdLine::SetArguments(-font => '10x20');
   Tk::CmdLine::SetArguments();
}elsif($opt_size eq "SXGA" || $opt_size eq "1280") {
   $minW = 1280;
   $minH = 1024;
   $smallsized = 0;
   $coef = 1.1; 
   Tk::CmdLine::SetArguments(-font => '8x13');
   Tk::CmdLine::SetArguments();
}elsif($opt_size eq "XGA" || $opt_size eq "1024") {
       $minW = 1024;
   $minH = 768;
   $smallsized = 1;
   $coef = 1; 
   Tk::CmdLine::SetArguments(-font => '7x14');
   Tk::CmdLine::SetArguments();
}elsif($opt_size eq "SVGA" || $opt_size eq "800") {
       $minW = 800;
   $minH = 600;
   $smallsized = 1;
   $coef = 0.64; 
   Tk::CmdLine::SetArguments(-font => '6x10');
   Tk::CmdLine::SetArguments();
}elsif($opt_size eq "VGA" || $opt_size eq "640"){
       $minW = 640;
   $minH = 480;
   $smallsized = 1;
   $coef = 0.48;
   Tk::CmdLine::SetArguments(-font => '5x7');
   Tk::CmdLine::SetArguments();
}

if($opt_undersize)
{
   $minW -= 10;
   $minH -= 30;
}

#=================================================================================
#
# H M I
#
#=================================================================================
my $mw = MainWindow->new();

# Set default min size
$mw->geometry($minW."x".$minH);
$mw->minsize($minW, $minH);
$mw->title($title);

# bitmaps
my $enlargebmp = $mw->Bitmap(-data => <<EOF);
#define expand_width 24
#define expand_height 16
static unsigned char expand_bits[] = {
  0x03, 0x42, 0xc0, 0x03, 0xc3, 0xc0, 0x83, 0xc3, 0xc1, 0xc3, 0xc3, 0xc3,
  0xe3, 0xc3, 0xc7, 0xf3, 0xc3, 0xcf, 0xfb, 0xc3, 0xdf, 0xff, 0xc3, 0xff,
  0xff, 0xc3, 0xff, 0xfb, 0xc3, 0xdf, 0xf3, 0xc3, 0xcf, 0xe3, 0xc3, 0xc7,
  0xc3, 0xc3, 0xc3, 0x83, 0xc3, 0xc1, 0x03, 0xc3, 0xc0, 0x03, 0x42, 0xc0,
  };

EOF

my $shrinkbmp = $mw->Bitmap(-data => <<EOF);
#define shrink_width 24
#define shrink_height 16
static unsigned char shrink_bits[] = {
  0x01, 0xc3, 0x80, 0x03, 0xc3, 0xc0, 0x07, 0xc3, 0xe0, 0x0f, 0xc3, 0xf0,
  0x1f, 0xc3, 0xf8, 0x3f, 0xc3, 0xfc, 0x7f, 0xc3, 0xfe, 0xff, 0xc3, 0xff,
  0xff, 0xc3, 0xff, 0x7f, 0xc3, 0xfe, 0x3f, 0xc3, 0xfc, 0x1f, 0xc3, 0xf8,
  0x0f, 0xc3, 0xf0, 0x07, 0xc3, 0xe0, 0x03, 0xc3, 0xc0, 0x01, 0xc3, 0x80,
  };

EOF

# Key-Tab binding is deactivated, because this event will be used by entries
# for completion functionality
$mw->bind('<Key-Tab>', sub {Tk->break});
my $focusedtext;
$mw->bind("Tk::Text", "<FocusIn>", [sub {
    &clearSearch() if $_[1] ne $focusedtext;
    $focusedtext = $_[1];}, Ev('W')]);

# create balloon help widget 
my $balloonhelp = $mw->Balloon(-balloonposition => 'mouse',
                               -state => 'none',
                );
# create base frames
my $top_fm =
    $mw->Frame()->pack(-fill => 'both',
                      -side => 'top',
                      -expand => 1,
                      -padx => 5*$coef, -pady => 5*$coef);
       
my $bottom_fm =
    $mw->Frame()->pack(-fill => 'both',
                      -side => 'bottom',
                      -expand => 0,
                      -padx => 5*$coef, -pady => 5*$coef,
                      );
my $bindings_fm =
    $bottom_fm->LabFrame(-label => 'Bindings : ',
                         -labelside => 'acrosstop',
                         -borderwidth => 3)->pack(-fill => 'both',
                         -side => 'left',
                         -padx => 5*$coef,
                         -expand => 0,
                         );
my $clients_fm =
    $bottom_fm->LabFrame(-label => 'Applications : ',
          -labelside => 'acrosstop',
          -borderwidth => 3)->pack(-fill => 'both',
                                   -side => 'left',
                                   -expand => 0,
                                   -padx => 5*$coef,
                                   );
my $send_fm =
    $bottom_fm->eLabFrame(-label => ' Messages to send : ',
           -buttonbmp => $enlargebmp, 
           -labelside => 'acrosstop',
           -borderwidth => 3)->pack(-fill => 'x',
                                    -side => 'left',
                                    -expand => 1,
                                    -padx => 5*$coef,
                                    );
my $sendEnlargeBtn = $send_fm->Subwidget('button');
$sendEnlargeBtn->configure(-relief => 'flat',
            -command => sub {
                if ($enlarge) {
                  &shrink;
                  $enlarge = 0;
                  $sendEnlargeBtn->configure(-image => $enlargebmp);
                } else {
                  &enlarge;
                  $enlarge = 1;
                  $sendEnlargeBtn->configure(-image => $shrinkbmp);
                }
            });

my $searchandcontrol_fm = $bottom_fm->Frame()->pack(-fill => 'both',
                                                    -side => 'right',
                                                    -expand => 0);
my $search_fm =
    $searchandcontrol_fm->LabFrame(-label => 'Search : ',
                                    -labelside => 'acrosstop',
                                    -borderwidth => 3)->pack(-fill => 'none',
                                                             -side => 'top',
                                                             -padx => 5*$coef,
                                                             -expand => 0);
my $control_fm =
    $searchandcontrol_fm->LabFrame(-label => 'Control : ',
                                    -labelside => 'acrosstop',
                                    -borderwidth => 3)->pack(-fill => 'none',
                                                             -side => 'bottom',
                                                             -padx => 5*$coef,
                                                             -expand => 0);

#----------------------------------------------------------------------------------
# Progress bar
#----------------------------------------------------------------------------------
my $tpl = $mw->Toplevel;
$tpl->Popup;
$tpl->raise($mw);
$tpl->title("");
$tpl->geometry("300x50");
my $progressbar = 
   $tpl->ProgressBar(-from => 0,
                      -length => 200,
                      -borderwidth => 2,
                      -colors => [ 0 => 'yellow'],
                      -relief => 'sunken',
                      -resolution => 0,
                      -anchor => 'w',
                      )->pack(-fill => 'both',
                               -expand => 1,
                               );
$progressbar->value(0);
$tpl->withdraw;

#----------------------------------------------------------------------------------
# Messages display area
#----------------------------------------------------------------------------------
my $top2_fm = $top_fm->Frame()->pack(-side => 'top',
                                      -fill => 'x',
                                      );
my $messagesLabel =
    $top2_fm->Label(-text => "Messages :")->pack(-side => 'left');

my $messagesCounterValue =
    $top2_fm->Label(-textvariable => \$recordedNumber,
                      -width => 8,
                      -anchor => 'w')->pack(-side => 'right');

my $messagesCounterLabel =
    $top2_fm->Label(-text => "Recorded :",
                      -width => 15,
                      -anchor => 'e')->pack(-side => 'right');

my $messagesBufdValue =
    $top2_fm->Label(-textvariable => \$bufNumber,
                      -width => 8,
                      -anchor => 'w')->pack(-side => 'right');

my $messagesBufLabel =
    $top2_fm->Label(-text => "Bufferized :",
                      -width => 17,
                      -anchor => 'e')->pack(-side => 'right');

my $messagesDeletedValue =
    $top2_fm->Label(-textvariable => \$deletedNumber,
                      -width => 8,
                      -anchor => 'w')->pack(-side => 'right');

my $messagesDeletedLabel =
    $top2_fm->Label(-text => "Skipped :",
                      -width => 14,
                      -anchor => 'e')->pack(-side => 'right');

my $messagesMaxLabel =
    $top2_fm->Label(-text => "Recordable : $history",
                      -width => 25)->pack(-side => 'right');
    
my $messagesText =
    $top_fm->Scrolled('Text',
                        -scrollbars => 'e',
                        -height => 18*$coef,
                        -spacing1 => 2,
                        -spacing2 => 0,
                        -spacing3 => 2,
                        -state => 'disabled',
                        )->pack(-fill => 'both',
                                -expand => 1,
                                -side => 'bottom');
                    
$focusedtext = $messagesText;
&wheelmousebindings($messagesText);
$messagesText->bind('<1>', sub {$messagesText->focus;});
$messagesText->tagBind('sender', '<Double-1>', \&marker);

my $bgcolor = $messagesText->cget(-background);

# text tag creation
$messagesText->tagConfigure('sender',
                            -background => 'gray50',
                            -foreground => 'gray90');
my @hide_option;
if ($Tk::VERSION ge 804) {
   @hide_option = (-elide => 1);
} else {
   @hide_option = (-state => 'hidden');
}
$messagesText->tagConfigure('time', @hide_option);
$messagesText->tagConfigure($appname, -foreground => 'gray30');
$messagesText->tagConfigure('marker0', -background => 'lightcoral',
                            -foreground => 'lightcoral');
$messagesText->tagConfigure('marker1', -background => 'LightGoldenrod',
                            -foreground => 'LightGoldenrod');
$messagesText->tagConfigure('marker2', -background => 'skyblue',
                            -foreground => 'skyblue');
$messagesText->tagConfigure('marker3', -background => 'darkseagreen',
                            -foreground => 'darkseagreen');
$messagesText->tagConfigure('marker4',
                            -background => 'ivory', -foreground => 'ivory');

for my $marker (qw(marker0 marker1 marker2 marker3 marker4)) {
    $messagesText->tagBind($marker, '<Double-1>', \&marker);
}


# check received messages speed and adjust drawing frequency
$messagesCounterValue->repeat(1000, sub {
    $messagesSpeed = $messagesNumber - $messagesLastNumber;
    $messagesLastNumber = $messagesNumber;
    # if update mechanism is off, we flush data here
    if ($noUpdateFlag) {
      $messagesText->see('end');
      $messagesCounterValue->update;
    }
    # if too many messages, update mechanism is unset
    if ($messagesSpeed > 50) {
      $noUpdateFlag = 1;
    #print "$messagesSpeed received messages/second !\n";
    # in nominal case, update mechanism is set
    # (each time a message is received, update is forced in Text window)
    } else {
      $noUpdateFlag = 0;
    }
});

#----------------------------------------------------------------------------------
# Bindings area
#----------------------------------------------------------------------------------
my $bindingsEntry =
    $bindings_fm->Entry(-width => 30*$coef)->pack(-fill => 'x',
                                                  -side => 'top',
                                                  -anchor => 'w',
                                                  -ipady => 3,
                                                  -expand => 0,
                                                  -pady => 5*$coef);
$bindingsEntry->bind('<Escape>' => [\&addBindingExpression]);
$bindingsEntry->bind('<Return>' => [\&addBinding]);

$bindingsEntry->bind('<Down>' => [\&bindingNextExpression]);
$bindingsEntry->bind('<Control-n>' => [\&bindingNextExpression]);
$bindingsEntry->bind('<Up>' => [\&bindingPrevExpression]);
$bindingsEntry->bind('<Control-p>' => [\&bindingPrevExpression]);

$bindingsEntry->focus;

my $bindingsList =
    $bindings_fm->Scrolled('Listbox',
                           -scrollbars => 'osoe',
                           -height => 4,
                           -width => 30*$coef)->pack(-fill => 'y',
                                     -side => 'top',
                                     -anchor => 'w',
                                     -expand => 0);
$bindingsList->bind('<1>', [\&selectBinding]);
$bindingsList->bind('<Double-1>', [\&addBinding]);
$bindingsEntry->bind('<Key>' => [\&findExprInList, $bindingsList]);

my $effectivebindingsList =
    $bindings_fm->Scrolled('Listbox',
                           -scrollbars => 'osoe',
                           -height => 4,
                           -width => 30*$coef)->pack(-fill => 'y',
                                     -side => 'bottom',
                                     -anchor => 'w',
                                     -expand => 1);
$effectivebindingsList->bind('<Double-1>', [\&removeBinding]);

&wheelmousebindings($bindingsList);

#----------------------------------------------------------------------------------
# Connected applications area
#----------------------------------------------------------------------------------
my $clientsListbox =
    $clients_fm->Scrolled('Listbox',
                          -height => 9,
                          -scrollbars => 'osoe')->pack(-fill => 'both',
                                                       -expand => 0,
                                                       -side => 'top');
$clientsListbox->bind('<1>', [\&selectClient]);
$clientsListbox->bind('<Double-1>', [\&showClientBindings]);
&wheelmousebindings($clientsListbox);

my $clientsButtons_fm =
    $clients_fm->Frame()->pack(-fill => 'both', -expand => 0, -side => 'bottom');



my $clientsSeeBindings_btn = 
    $clientsButtons_fm->Button(-command => [\&showClientBindings],
                               -text => 'See bindings',
                               )->pack(-side => 'top', -fill => 'both', 
                                       -expand => 1);
my $clientsButtons2_fm =
    $clientsButtons_fm->Frame()->pack(-fill => 'both', -expand => 1, 
                                      -side => 'top');
my $clientsFilter_btn = 
    $clientsButtons2_fm->Button(-command => [\&filterClient],
                                  -text => "Filter",
                                  )->pack(-side => 'left', -fill => 'both', 
                                          -expand => 1);

my $clientsKill_btn = 
    $clientsButtons2_fm->Button(-command => [\&killClient],
                                  -text => "Kill",
                                 )->pack(-side => 'left', -fill => 'both', 
                                          -expand => 1);

#----------------------------------------------------------------------------------
# Messages to send area
#----------------------------------------------------------------------------------

my $send1_fm = $send_fm->Frame()->pack(-side => 'top',
                                        -expand => 1,
                                        -fill => 'both');
my $sendEntry =
    $send1_fm->Entry(-width => 40*$coef)->pack(-fill => 'x',
                                                -side => 'left',
                                                -anchor => 'w',
                                                -ipady => 3,
                                                -expand => 1,
                                                -pady => 0);
my $recipientLabel =
    $send1_fm->Label(-width => 2,
           -relief => 'sunken')->pack(-fill => 'x',
                                       -side => 'left',
                                       -anchor => 'w',
                                       -ipady => 3,
                                       -pady => 0);

my $send2_fm = $send_fm->Frame()->pack(-side => 'top',
                                        -pady => 5*$coef,
                                        -expand => 1,
                                        -fill => 'both');

my $clockstartButtonmsg = "Clock Start";
my $clockstopButtonmsg = "Clock Stop";
my $clockbackCheckButtonmsg = "Backward";

if($smallsized){
#   $clockstartButtonmsg = "Start";
   $clockstartButtonmsg = "Go";
   $clockstopButtonmsg = "Stop";
   $clockbackCheckButtonmsg = "Backw";
}

my $clockstartButton = 
    $send2_fm->Button(-height => 1,
            -command => [\&clockstart],
            -text => $clockstartButtonmsg)->grid(-column => 1,
                                                 -row => 1);
my $clockstopButton = 
    $send2_fm->Button(-height => 1,
            -command => [\&clockstop],
            -text => $clockstopButtonmsg)->grid(-column => 2,
                                                -row => 1);
my $clockbackCheckbutton = 
    $send2_fm->Checkbutton(-selectcolor => undef,
                           -command => [\&clockswitch],
                           -variable => \$clockbackwardflag,
                           -text => $clockbackCheckButtonmsg,
                           -indicatoron => 0)->grid(-column => 3,
                                                      -row => 1,
                                                      -ipadx => 10*$coef,
                                                      -sticky => 'nsew');
my $sendList =
    $send_fm->Scrolled('Listbox',
                         -scrollbars => 'osoe',
                         -height => 9,
                         -width => 40*$coef)->pack(-fill => 'both',
                                                    -anchor => 'w',
                                                    -side => 'top',
                                                    -expand => 1);
$sendEntry->bind('<Key>' => [\&findExprInList, $sendList]);
$sendEntry->bind('<Return>' => [\&addMsgToSend, undef, 1]);

$sendEntry->bind('<Down>' => [\&sendNextExpression]);
$sendEntry->bind('<Control-n>' => [\&sendNextExpression]);
$sendEntry->bind('<Up>' => [\&sendPrevExpression]);
$sendEntry->bind('<Control-p>' => [\&sendPrevExpression]);

$sendList->bind('<1>', [\&selectMsgToSend]);
$sendList->bind('<Double-1>', [\&addMsgToSend, undef, 1]);
&wheelmousebindings($sendList);


#----------------------------------------------------------------------------------
# Search area
#----------------------------------------------------------------------------------
my $searchEntry =
    $search_fm->Entry(-width => 24)->grid(-column => 1,
                                            -row => 1,
                                            -columnspan => 3);
$searchEntry->eventAdd('<<SearchNext>>', '<Return>', '<Next>',
                         '<Control-s>', '<Control-f>');
$searchEntry->eventAdd('<<SearchPrev>>', '<Shift-Return>', '<Prior>',
                         '<Control-r>');
$searchEntry->eventAdd('<<SearchNextExpr>>', '<Down>', '<Control-n>');
$searchEntry->eventAdd('<<SearchPrevExpr>>', '<Up>', '<Control-p>');

$searchEntry->bind('<<SearchNext>>' => [\&searchNext, 1]);
$searchEntry->bind('<<SearchPrev>>' => [\&searchPrev, 1]);
$searchEntry->bind('<<SearchNextExpr>>' => [\&searchNextExpression]);
$searchEntry->bind('<<SearchPrevExpr>>' => [\&searchPrevExpression]);
$searchEntry->bind('<Key>' => [\&searchOnTheFly]);

my $checkbutt_fm =
    $search_fm->Frame()->grid(-column => 1,
                              -row => 2,
                              -columnspan => 3);
my $casesensitive_cb =
    $checkbutt_fm->Checkbutton(-text => 'Case sens.',
                               -variable => \$casesensitiveflag
                               )->grid(-column => 1,
                                        -row => 1);
my $regexp_cb =
    $checkbutt_fm->Checkbutton(-text => 'Regexp',
                               -variable => \$regexpflag
                                  )->grid(-column => 2,
                                           -row => 1);

my $searchPrev =
    $search_fm->Button(-text => 'Prev',
                         -height => 1,
                         -width => 4,
                         -command => [\&searchPrev])->grid(-column => 1,
                                                          -row => 3);
my $searchNext =
    $search_fm->Button(-text => 'Next',
                         -width => 4,
                         -height => 1,
                         -command => [\&searchNext])->grid(-column => 2,
                                                          -row => 3);
my $searchAll =
    $search_fm->Button(-text => 'All',
                         -width => 4,
                         -height => 1,
                         -command => [\&searchAll])->grid(-column => 3,
                                                            -row => 3);
#----------------------------------------------------------------------------------
# Control panel
#----------------------------------------------------------------------------------

my $balloonflag = 0;
my $balloon_cb = 
$control_fm->Checkbutton(-text => 'Balloon help',
                         -variable => \$balloonflag,
                         -command => sub {
                             if ($balloonflag == 0) {
                               $balloonhelp->configure(-state => 'none');
                             } else {
                               $balloonhelp->configure(-state => 'balloon');
                             }
                         }
                         );
$balloon_cb->grid(-column => 1,
                    -columnspan => 2,
                    -row => 1);
my $exitButton =
    $control_fm->Button(-height => 2,
                        -width => 4,
                        -command => [\&bye],
                        -text => 'Exit')->grid(-column => 3,
                                                 -row => 1);

my $loadButton =
    $control_fm->Button(-height => 2,
                        -width => 4,
                        -command => [\&loadfile, 1],
                        -state => 'normal',
                        -text => 'Load')->grid(-column => 1,
                                                 -row => 2);
my $saveButton =
    $control_fm->Button(-height => 2,
                        -width => 4,
                        -command => [\&savefile, 1],
                        -state => 'normal',
                        -text => 'Save')->grid(-column => 2,
                                               -row => 2);

my $jumpButton =
    $control_fm->Button(-height => 2,
                        -width => 4,
                        -command => [\&jump],
                        -state => 'disabled',
                        -text => 'Jump')->grid(-column => 3,
                                                 -row => 2);

my $startButton =
    $control_fm->Button(-height => 2,
                        -width => 4,
                        -command => [\&start, 1],
                        -state => 'disabled',
                        -text => 'Scroll')->grid(-column => 1,
                                                 -row => 3);
my $stopButton =
    $control_fm->Button(-height => 2,
                        -width => 4,
                        -command => [\&stop, 1],
                        -text => "Scroll\nLock")->grid(-column => 2,
                                                        -row => 3);
my $clearButton =
    $control_fm->Button(-height => 2,
                        -width => 4,
                        -command => [\&clear, 1],
                        -state => 'normal',
                        -text => 'Clear')->grid(-column => 3,
                                                -row => 3);
#----------------------------------------------------------------------------------
# Update default bindings and messages lists
#----------------------------------------------------------------------------------
# add default bindings in available bindings list
for my $bind (sort(@bind_def)) {
    &addBindingInList($bind);
}

# add default messages in available messages list
for my $msg (sort(@send_def)) {
    &addMsgToSend(undef, $msg);
}


#----------------------------------------------------------------------------------
# Balloon help messages
#----------------------------------------------------------------------------------

my $completionText =
    "Provides completion functionality : type part    \n".
    "of a word and hit the [Tab] key to activate      \n".
    "completion.                                      ";

my $historyText =
    "Provides history functionality : use [Up] and    \n".
    "[Down] keys to navigate in inputs history.       ";

my $insertionText =
    "Also makes values entries easier : when you hit  \n".
    "the [Tab] key, the insertion cursor move to the  \n".
    "next rvalue field, and select it if defined.     ";

$balloonhelp->attach($messagesMaxLabel, -balloonmsg =>
           "Maximum number of recordable messages\n".
           "(=history size value)                "
           );
$balloonhelp->attach($messagesDeletedLabel, -balloonmsg =>
           "Counter for received messages which are\n".
           "skipped when history size is reached.  "
           );
$balloonhelp->attach($messagesBufLabel, -balloonmsg =>
           "Counter for received messages which are bufferized\n".
           "when [Scroll Lock] button is active. They will be \n".
           "displayed when user will restore scroll mode.     "
           );
$balloonhelp->attach($messagesCounterLabel, -balloonmsg =>
           "Counter for received and displayed messages."
           );
$balloonhelp->attach($bindingsEntry, -balloonmsg =>
           "This input field is used to enter new bindings or\n".
           "edit default one from list above. In both case,  \n".
           "hit [Enter] to apply.                            \n".
           $historyText."\n".
           $completionText."\n".
           $insertionText
           );
$balloonhelp->attach($bindingsList, -balloonmsg =>
           "Available bindings list. Select an item for edition or\n".
           "double-click on a predefined binding to activate it.  "
           );

$balloonhelp->attach($effectivebindingsList, -balloonmsg =>
           "Effective bindings list. Double-click on binding to unset it."
           );
$balloonhelp->attach($clientsListbox,-balloonmsg =>
           "Select an application name to highlight related\n".
           "Ivy messages in the Messages area.             \n".
           "Double-click on it to display the application  \n".
           "bindings or click on the 'See bindings' button.\n".
           "To display messages in a separate window, click\n".
           "on the 'Filter' button.                        \n".
           "To kill an agent, click on the 'Kill' button.  ");
$balloonhelp->attach($sendEntry, -balloonmsg =>
           "This input field is used to enter new messages or\n".
           "edit default one from list above. In both case,  \n".
           "hit [Enter] to send it.                          \n".
           $historyText."\n".
           $completionText."\n".
           $insertionText
           );
$balloonhelp->attach($sendList, -balloonmsg =>
           "Available messages list. Select an item for edition\n".
           "or double click on predefined message to send it.  "
           );
$balloonhelp->attach($recipientLabel, -balloonmsg =>
           "Number of last message recipients."
           );
$balloonhelp->attach($messagesText, -balloonmsg =>
           "You can insert colored marker by double-clicking on a \n".
           "message application name (marker will be createafter\n".
           "the message). Then, you can quickly access markers    \n".
           "using the [Jump] button in control panel. To remove a \n".
           "marker, just double-click on it.                      "
           );
$balloonhelp->attach($searchEntry, -balloonmsg =>
           "This input field is used to search expressions in \n".
           "messages list. Hit [Return], [Ctrl-s] or [Ctrl-f]\n".
           "key to search forward and [Shift-Return] or [Ctrl-r]\n".
           "key to search backward.                           \n".
           $historyText
           );
$balloonhelp->attach($stopButton, -balloonmsg =>
           "Stop scrolling in Messages area."
           );
$balloonhelp->attach($startButton, -balloonmsg =>
           "Restart scrolling in Messages area."
           );
$balloonhelp->attach($jumpButton, -balloonmsg =>
           "Access next marker in Messages area."
           );
$balloonhelp->attach($clearButton, -balloonmsg =>
           "Remove messages displayed\n".
           "in Messages area.        "
           );
$balloonhelp->attach($loadButton, -balloonmsg =>
           "Load messages file and display\n".
           "its content in Messages area. "
           );
$balloonhelp->attach($saveButton, -balloonmsg =>
           "Save the content of Messages\n".
           "area in a file.             "
           );
$balloonhelp->attach($sendEnlargeBtn, -balloonmsg =>
           "Click here to enlarge or shrink this frame"
           );

#=================================================================================
#
# Ivy initialisation and Ivy bindings
#
#=================================================================================
# add Ivymon in connected applications list
&addClient($appname, 'localhost');

# init Ivy bus and start it.
Ivy->init(-loopMode => 'TK',
          -appName => $appname,
          -ivyBus => $ivy_port,
          -onDieFunc => [\&quit],
          );   
my $ivy = Ivy->new(-statusFunc  => \&checkClientsStatus);
$ivy->start;

# add Ivy bindings
for my $bind (@effectivebind) {
    &addBinding(undef, $bind);
}

#=================================================================================
#
# Options and arguments test
#
#=================================================================================

# load input files
if (@ARGV > 0) {
    my $file = $ARGV[0];
    if ( not open(IN, $file)) {
      $mw->Tk::Error("Can't open file '$file' ($!)");
    } else {
      &showProgressbar();
      my $step = &stepsnumber();
      &loadfileForDisplay($step);
      &hideProgressbar();
      close(IN);
    }
}

$ivy->bindRegexp("^Ivymon Output=(.*)", [sub { $outputfile = $_[1]; }]);
MainLoop;

#==================================================================================
#
#  Functions 
#
#==================================================================================

#----------------------------------------------------------------------------------
# Ivy functions
#----------------------------------------------------------------------------------
sub addIvyBinding {
   my $binding = shift;
   #print "in addIvyBinding $binding\n";
   $ivy->bindRegexp($binding, [sub {
      my $sender = shift;
      my $message;
      if (@_ > 1) {
         for (my $i = 0; $i < @_; $i++) {
            $_[$i] = '"'.$_[$i].'"';
         }
         $message = join(' ', @_);
      } else {
         $message = shift;
      }
      $messagesNumber++;
      my $time = gettimeofday();
      if ($stopFlag) {
         &bufferizeMessages($sender, $message, $time);
      } else {
         &beforeUpdatingMessages;
         &updateMessages($sender, $message, $time);
         &afterUpdatingMessages;
      }
   }]);

} # end addIvyBinding


sub removeIvyBinding {
   my $binding = shift;
   $ivy->bindRegexp($binding);

} # end removeIvyBinding


#----------------------------------------------------------------------------------
# Functions related to connected applications management
#----------------------------------------------------------------------------------
sub checkClientsStatus {

   my $appname = $_[3];
   my $status = $_[4];
   my $host;
   my $regexp;
   my $newapi;
   $newapi = 1 if (($ivy_cvsrevision == 1 and $ivy_version ge 1.40) or
                  ($ivy_cvsrevision == 0 and $ivy_version ge 4.18));
   if ($newapi) {
      $host = $_[5];
      $regexp = $_[6];
   } else {
      $host = $_[5];
      $regexp = $host;
   }
   $host =~ s/:.*//;
   #print "status=$status host=$host regexp=$regexp\n";
   $appname =~ s/ /_/g;
   if ($status eq 'died') {
      &removeClient($appname, $host);
   } elsif ($status eq 'new') {
      &addClient($appname, $host);
   } elsif ($status eq 'subscribing') {
      $clientsBindings{$appname}->{$regexp}++;
      &addBindingAndMsgToSend($regexp);
   } elsif ($status eq 'unsubscribing') {
      $clientsBindings{$appname}->{$regexp}--;
   } else {
      carp "In Ivymon, checkClientsStatus function, unknown status <$status>\n";
   }

} # end checkClientsStatus


sub addClient {
    my $client = shift;
    my $host = shift;
    $host = $hostname if $host eq 'localhost' or $host =~ /^$hostname\./;
    #print "addClient $client on $host\n";
    $connectedClients{$client}->{$host}++;
    &manageClient($client, $host);

} # end addClient


sub removeClient {
    my $client = shift;
    my $host = shift;
    $host = $hostname if $host eq 'localhost' or $host =~ /^$hostname\./;
    $connectedClients{$client}->{$host}-- if $connectedClients{$client}->{$host} > 0;
    my $num = 0;
    for my $host ((keys(%{$connectedClients{$client}}))) {
      $num += $connectedClients{$client}->{$host};
    }
    delete $clientsBindings{$client} if $num == 0;
    &manageClient($client, $host);
  
}  # end removeClient


sub manageClient {
   my $client = shift;
   my $host = shift;
   my $i = 0;
   for ($clientsListbox->get(0, 'end')) {
   if ($_ =~ /^$client/ or $_ =~ /-- $client/) {
       $clientsListbox->delete($i);
       last;
   }
   $i++;
   }
   my $num = 0;
   for (values(%{$connectedClients{$client}})) {
      $num += $_;
   }
   if ($num == 0) {
      $clientsListbox->insert('end', "-- $client died");   
   } elsif ($num == 1) {
      $clientsListbox->insert($i, "$client on $host");
   } elsif ($num > 1) {
      my $msg = "$client ";
      for my $host ((keys(%{$connectedClients{$client}}))) {
          if ($connectedClients{$client}->{$host} == 0) {
            next;
          } elsif ($connectedClients{$client}->{$host} == 1) {
            $msg .= "on $host, ";
          } else {
            $msg .= "on $host($connectedClients{$client}->{$host}), ";
          }
      }
      $msg =~ s/, $//;
      $clientsListbox->insert($i, $msg);
   }
    
} # end manageClient

sub killClient {
    my $selindex = $clientsListbox->curselection;
    return unless defined $selindex;
    my $client = $clientsListbox->get($selindex);
    $client =~ s/^-- //;
    $client =~ s/\(\d+\)$//;
    $client =~ s/\s.*//;
    $ivy->sendDieTo($client);
    
    
} # end killClient

sub selectClient {
   $messagesText->tagConfigure($selectedClient, -background => $bgcolor)
      if $selectedClient;
   my $selindex = $clientsListbox->curselection;
   my $client = $clientsListbox->get($selindex);
   $client =~ s/^-- //;
   $client =~ s/\(\d+\)$//;
   $client =~ s/\s.*//;
   if ($selectedClient eq $client) {
      $selectedClient = undef;
      $clientsListbox->selectionClear($selindex);
      return;
   }
   $messagesText->tagConfigure($client, -background => 'gray70');
   $selectedClient = $client;

} # end selectClient


sub filterClient {
   my $selindex = $clientsListbox->curselection;
   return unless defined $selindex;
   my $client = $clientsListbox->get($selindex);
   $client =~ s/^-- //;
   $client =~ s/\(\d+\)$//;
   $client =~ s/\s.*//;
   # si une fenetre client existe, on la raise
   if (defined $clientsMessagesTpl{$client} and
      Tk::Exists $clientsMessagesTpl{$client}) {
      $clientsMessagesTpl{$client}->toplevel->raise;
      return;
   # sinon, on la crée
   } else {
      my $tpl = $mw->Toplevel;
      $tpl->group($mw);
      my $title = "$client messages";
      $tpl->title($title);
      $tpl->Label(-text => $title)->pack(-side => 'top',
                                          -padx => 10,
                                          -pady => 10);
      $clientsMessagesTpl{$client} =
         $tpl->Scrolled('Text',
         -scrollbars => 'e',
         )->pack(-side => 'top',
                  -fill => 'both',
                  -expand => 1);
      $clientsMessagesTpl{$client}->bind('<1>', sub {
         $clientsMessagesTpl{$client}->Subwidget('scrolled')->focus;});
      
      &wheelmousebindings($clientsMessagesTpl{$client});
      
      $tpl->Button(-text => 'Close',
                     -command => [sub {
                        my $text = 
                           $clientsMessagesTpl{$client}->Subwidget('scrolled');
                        if ($focusedtext eq $text) {
                           $focusedtext = $messagesText;
                           $messagesText->focus;
                        }
                        $clientsMessagesTpl{$client} = undef;
                        $tpl->destroy;
                     }],
                     )->pack(-side => 'top',
                              -padx => 10,
                              -pady => 10);
   }
   
   my @list = $messagesText->tagRanges($client);
   for (my $i=0; $i < @list; $i += 2) {
      &updateClientMessages($client, $messagesText->get($list[$i], $list[$i+1]));
   }
   $clientsMessagesTpl{$client}->configure(-state => 'disabled');

} # end filterClient



sub showClientBindings {
   my $selindex = $clientsListbox->curselection;
   return unless defined $selindex;
   
   my $client = $clientsListbox->get($selindex);
   $client =~ s/\(\d+\)$//;
   $client =~ s/ on .*//;
   my $tpl = $clientBindingsTpl{$client};
   $tpl->destroy if defined $tpl and Tk::Exists($tpl);
   $tpl = $mw->Toplevel;
   $clientBindingsTpl{$client} = $tpl;
   my $title = "Ivy bindings of $client application";
   $tpl->title($title);
   $tpl->Label(-text => $title)->pack(-side => 'top',
               -padx => 10,
               -pady => 10);
   my $t = $tpl->Scrolled('Text',
                           -scrollbars => 'e',
                           )->pack(-side => 'top',
                                    -fill => 'both',
                                    -expand => 1);
   $t->tagConfigure('0', -background => '#d4d4d4');
   $t->tagConfigure('1', -background => '#e5e5e5');
   my $i = 0;
   for my $regexp (sort keys(%{$clientsBindings{$client}})) {
      #print "client=$client regexp=$regexp\n";
      $t->insert('end', $regexp."\n", $i % 2)
         if $clientsBindings{$client}->{$regexp} > 0;
      $i++;
   }
   $t->configure(-state => 'disabled');
   $tpl->Button(-text => 'Close',
                  -command => [sub {$tpl->destroy;
                                 $clientsListbox->selectionClear($_[0])
                                 unless $client eq $selectedClient;
                                 }, $selindex, $client],
                  )->pack(-side => 'top',
                           -padx => 10,
                           -pady => 10);

    
} # end showClientBindings


#----------------------------------------------------------------------------------
# Functions related to messages display management
#----------------------------------------------------------------------------------
sub marker {
   return if $noMessageYet;
   my @markers =  $messagesText->markNames;
   my $foundmarker;
   my @markers2;
   for (@markers) {
      push(@markers2, $_) if (/^ivymon/);
   }
   $jumpButton->configure(-state => 'disabled') if @markers2 == 0;
   for(@markers2) {
      my $comp = $messagesText->compare('current linestart', '==', $_);
      if ($comp) {
         $foundmarker = $_;
         $jumpButton->configure(-state => 'disabled') if @markers2 == 1;
         last;
      }
   }
   if ($foundmarker) {
      &deletemarker($foundmarker);
   } else {
      my $index = $messagesText->index('current linestart + 1 lines');
      my $nb = $markers_cnt % 5;
      &addmarker($index, 'marker'.$nb);
      $markers_cnt++;
   }
   
} # end marker


sub deletemarker {
    my $marker = shift;
    $messagesText->configure(-state => 'normal');
    $messagesText->delete($marker, "$marker lineend + 1 chars");   
    $messagesText->markUnset($marker);
    $messagesText->configure(-state => 'disabled');
    
} # end deletemarker


sub addmarker {
    my $index = shift;
    my $marker = shift;
    #print "index=$index marker=$marker\n";
    $messagesText->configure(-state => 'normal');
    $messagesText->insert($index, "$marker\n");
    $messagesText->tagAdd($marker, $index, "$index + ".(length($marker)+1)." chars");
    $messagesText->markSet('ivymon'.$markers_cnt, $index);
    $jumpButton->configure(-state => 'normal');
    $messagesText->configure(-state => 'disabled');

} # ens addmarker


sub bufferizeMessages {
    my ($sender, $message, $time) = @_;
    $bufNumber++;
    # if scrolling is locked, don't force display, just put message in buffer
    push(@messagesbuffer, [$sender, $message, $time]);
    splice(@messagesbuffer, 0, 1) if @messagesbuffer > $history;

} # end bufferizeMessages


sub beforeUpdatingMessages {
    $messagesText->configure(-state => 'normal');

} # end beforeUpdatingMessages


sub updateMessages {
    my ($sender, $message, $time) = @_;
    chomp($message);
    $message = '"' . $message . '"' ;
    # If scrolling is available, format and then display message.
    # Unless file has been loaded, look at history : compare widget text lines
    # number with history size
    if ($loadedFileFlag) {
      &loadMessage($sender, $message, $time);
      $messagesText->update unless $noUpdateFlag;

    } else {
      my ($linesNb) = split(/\./, $messagesText->index('end'));
      $linesNb--;
      #print "linesNb=$linesNb\n";
      if ($linesNb > $history) {
         $deletedNumber += 1;
         if ($deletedNumber == 1) {
            $messagesDeletedLabel->configure(-foreground => 'red');
            $messagesDeletedValue->configure(-foreground => 'red');
         }
      } else {
          &loadMessage($sender, $message, $time);
          $messagesText->update unless $noUpdateFlag;
      }
    }

} # end updateMessages


sub updateClientMessages {
    my ($sender, $message) = @_;
    return unless $clientsMessagesTpl{$sender} and
      Tk::Exists $clientsMessagesTpl{$sender};
    $clientsMessagesTpl{$sender}->configure(-state => 'normal');
    $clientsMessagesTpl{$sender}->insert('end', $message);
    $clientsMessagesTpl{$sender}->insert('end', "\n");
    $clientsMessagesTpl{$sender}->update unless $noUpdateFlag;
    $clientsMessagesTpl{$sender}->see('end');
    $clientsMessagesTpl{$sender}->configure(-state => 'disabled');
    
} # end updateClientMessages


sub loadMessage {
    my ($sender, $message, $time) = @_;
    $recordedNumber++;
    $noMessageYet = 0;
    my $text = "$sender $time $message\n";
    my $index1 = $messagesText->index('end')." - 1 lines";
    my $index2 = "$index1 + ".length($sender)." chars";
    $messagesText->insert('end', $text);
    $messagesText->tagAdd('sender', $index1, $index2);
    my $index3 = "$index2 + 1 chars";
    my $index4 = "$index3 + ".length($time)." chars";
    $messagesText->tagAdd('time', $index3, $index4);
    my $index5 = "$index4 + 1 chars";
    my $index6 = "$index5 + ".length($message)." chars";
    $messagesText->tagAdd($sender, $index5, $index6);
    &updateClientMessages($sender, $message) if $clientsMessagesTpl{$sender} and
      Tk::Exists $clientsMessagesTpl{$sender};
    
    
} # end loadmessages


sub afterUpdatingMessages {
    $messagesText->see('end');
    $messagesText->configure(-state => 'disabled');

} # end afterUpdatingMessages


sub highlightString {
    my ($i1, $i2) = @_;
    $focusedtext->tagConfigure('found',
                               -background => 'sienna',
                               -foreground => 'ivory');
    $focusedtext->tagAdd('found', $i1, $i2);
    
} # end highlightString


sub highlightStringOff {
    $focusedtext->tagDelete('found');
    $focusedtext->tagConfigure('found',
                               -background => 'sienna',
                               -foreground => 'ivory');
} # end highlightStringOff


#----------------------------------------------------------------------------------
#Functions related to Ivy bindings management
#----------------------------------------------------------------------------------
sub addBindingExpression {
    $bindingsEntry->insert('insert', '(.*)');
    $bindingsEntry->xview('end');

} # end addBindingExpression


sub bindingNextExpression {
   my $cursorIndex = $bindingsEntry->index('insert');
   $cursorIndex = -1 if $bindingsEntry->index('end') == $cursorIndex;
   if ($bindHistoryIndex == -1 or $bindHistoryIndex == @bindHistory - 1 
         or @bindHistory <= 1) {
      &warning1($bindingsEntry);
   } else {
      $bindHistoryIndex++;
      $bindingsEntry->delete(0, 'end');
      $bindingsEntry->insert(0, $bindHistory[$bindHistoryIndex]);
      $bindingsEntry->icursor($cursorIndex) if $cursorIndex >= 0;
   }

} # end bindingNextExpression


sub bindingPrevExpression {
    my $cursorIndex = $bindingsEntry->index('insert');
    $cursorIndex = -1 if $bindingsEntry->index('end') == $cursorIndex;
    if ($bindHistoryIndex == 0 or @bindHistory <= 1) {
      &warning1($bindingsEntry);
    } else {
      if ($bindHistoryIndex == -1) {
          $bindHistoryIndex = @bindHistory - 2;
      } else {
          $bindHistoryIndex--;
      }
      $bindingsEntry->delete(0, 'end');
      $bindingsEntry->insert(0, $bindHistory[$bindHistoryIndex]);
      $bindingsEntry->icursor($cursorIndex) if $cursorIndex >= 0;
    }

} # end bindingPrevExpression


sub clearBinding {
    $bindingsEntry->delete(0, 'end');
    $bindingsList->selectionClear(0, 'end');

} # end clearBinding


sub addBinding {
    my ($sender, $entry) = @_;
    unless ($entry) {
      $entry = $bindingsEntry->get;
      return unless $entry;
    }
    &bindHistoryGenList($entry);
    if ($effectivebindings{$entry}) {
      &warning2($bindingsEntry);
      return ;
    }
    $effectivebindingsList->insert('end', "bound to ".$entry);
    $effectivebindings{$entry} = 1;
    &addBindingInList($entry);
    &warning3($bindingsEntry);
    &addIvyBinding($entry);
    &bindingsGenList;
    $clientsBindings{$appname}->{"$entry"}++;


} # end addBinding


sub addBindingInList {
    my $entry = shift;
    return if $bindings{$entry};
    my @content = $bindingsList->get(0, 'end');
    push (@content, $entry);
    my $index = 0;
    my $i = 0;
    for (sort @content) {
      $index = $i if $_ eq $entry;
      $i++;
    }
    $bindingsList->insert($index, $entry);
    &bindingsGenList;

} # end addBindingInList


# add a binding in the bindings list and, after some modifications,
# add a new message in the list of messages to send.
sub addBindingAndMsgToSend {
    my $regexp = shift;
    return if $regexp eq '(.*)';
    
    ### transform regexp ###
    
    # skip ^ and $ characters in regexp
    $regexp =~ s/^\^//;
    $regexp =~ s/\$$//;
    my $msg = $regexp;
    # skip parenthesis in regexp
    $regexp =~ s/\(//g;
    $regexp =~ s/\)//g;
    # add global parenthesis 
    $regexp = "^(".$regexp.")";
    &addBindingInList($regexp);
    
    ### extract message to send from regexp ###

    # skip expressions between parenthesis
    $msg =~ s/=\(.*?\)/=/g;
    $msg =~ s/\s+$//;

    # duplicate expressions containing the | character
    my @expr;
    my $msgsnum = 1;
    while ($msg =~ /\((.*?\|.*?)\)/g) {
      push(@expr, $1);
      $msgsnum *= split(/\|/, $1);
    }
    my @msgs;
    for (my $i=0; $i < $msgsnum; $i++) {
      push(@msgs, $msg);
    }
    if ($msgsnum > 1) {
      for my $expr (@expr) {
          my (@field) = split(/\|/, $expr);
          my $j = 0;
          for (my $i=0; $i < @msgs; $i++) {
            my $qmexpr = quotemeta($expr);
            $msgs[$i] =~ s/\($qmexpr\)/$field[$j]/;
            $j++;
            $j = 0 if $j == @field;
          }
      }
      for (@msgs) {
          &addMsgToSend(undef, $_);
      }
    } else {
      &addMsgToSend(undef, $msg);
    }

} # end addBindingAndMsgToSend


sub removeBindingAndMsgToSend {

    my $regexp = shift;
    my $host = shift;

} # end removeBindingAndMsgToSend


sub selectBinding {
    $bindingsEntry->focus;
    my $selindex = $bindingsList->curselection;
    my $selected = $bindingsList->get($selindex);
    $bindingsEntry->delete(0, 'end');
    $bindingsEntry->insert(0, $selected);
    
} # end selectBinding


sub removeBinding {
    my $selindex = $effectivebindingsList->curselection;
    return unless defined($selindex);
    my $selected = $effectivebindingsList->get($selindex);
    $selected =~ s/^bound to //;
    &removeIvyBinding($selected);
    $effectivebindingsList->delete($selindex);
    $effectivebindings{$selected} = undef;
    $clientsBindings{$appname}->{"$selected"}--;

    
} # end removeBinding


sub bindingsGenList {
    my $i = 0;
    my $found = 0;
    for (keys %bindingsIndex) {
      delete $bindingsIndex{$_};
      delete $bindings{$_};
    }
    for ($bindingsList->get(0, 'end')) {
      $found = 1;
      $bindingsIndex{$_} = $i++;
      $bindings{$_} = 1
    }
    $bindingsFlag = $found;

} # end bindingsGenList


sub bindHistoryGenList {
    my $string = shift;
    return if $bindHistory{$string};
    $bindHistory{$string} = 1;
    push(@bindHistory, $string);
    
} # end bindHistoryGenList

#----------------------------------------------------------------------------------
# Fucntions related to messages emission.
#----------------------------------------------------------------------------------

sub sendNextExpression {
    my $cursorIndex = $sendEntry->index('insert');
    $cursorIndex = -1 if $sendEntry->index('end') == $cursorIndex;
    if ($sendHistoryIndex == -1 or $sendHistoryIndex == @sendHistory - 1
        or @sendHistory <= 1) {
      &warning1($sendEntry);
    } else {
      $sendHistoryIndex++;
      $sendEntry->delete(0, 'end');
      $sendEntry->insert(0, $sendHistory[$sendHistoryIndex]);
      $sendEntry->icursor($cursorIndex) if $cursorIndex >= 0;
    }

} # end sendNextExpression


sub sendPrevExpression {
    my $cursorIndex = $sendEntry->index('insert');
    $cursorIndex = -1 if $sendEntry->index('end') == $cursorIndex;
    if ($sendHistoryIndex == 0 or @sendHistory <= 1) {
      &warning1($sendEntry);
    } else {
      if ($sendHistoryIndex == -1) {
          $sendHistoryIndex = @sendHistory - 2;
      } else {
          $sendHistoryIndex--;
      }
      $sendEntry->delete(0, 'end');
      $sendEntry->insert(0, $sendHistory[$sendHistoryIndex]);
      $sendEntry->icursor($cursorIndex) if $cursorIndex >= 0;
    }

} # end sendPrevExpression


sub addMsgToSend {
    my ($sender, $entry, $sendFlag) = @_;
    unless ($entry) {
      $entry = $sendEntry->get;
      return unless $entry;
    }
    &sendMsg($entry) if $sendFlag;
    &warning3($sendEntry);
    &sendHistoryGenList($entry) if $sendFlag;
    return if $msgToSend{$entry};
    my @content = $sendList->get(0, 'end');
    push (@content, $entry);
    my $index = 0;
    my $i = 0;
    for (sort @content) {
      $index = $i if $_ eq $entry;
      $i++;
    }
    $sendList->insert($index, $entry);
    &sendGenList;

} # end addMsgToSend


sub sendMsg {
    my $entry = shift;
    $messagesNumber++;
    &beforeUpdatingMessages;
    &updateMessages($appname, $entry, scalar gettimeofday());
    &afterUpdatingMessages;
    my $c = $ivy->sendMsgs($entry);
    $recipientLabel->configure(-text => $c);
    
} # end sendMsg


sub selectMsgToSend {
    $sendEntry->focus;
    $recipientLabel->configure(-text => '');
    my $selindex = $sendList->curselection;
    return unless defined $selindex;
    my $selected = $sendList->get($selindex);
    $sendEntry->delete(0, 'end');
    $sendEntry->insert(0, $selected);
    my $index = index($selected, '=');
    return if $index < 0;
    $sendEntry->icursor($index+1);

} # end selectMsgToSend


sub sendGenList {
    my $i = 0;
    my $found = 0;
    for (keys %msgToSendIndex) {
      delete $msgToSendIndex{$_};
      delete $msgToSend{$_};
    }
    for ($sendList->get(0, 'end')) {
      $found = 1;
      $msgToSendIndex{$_} = $i++;
      $msgToSend{$_} = 1
    }
    $msgToSendFlag = $found;

} # end sendGenList


sub sendHistoryGenList {
    my $string = shift;
    return if $sendHistory{$string};
    $sendHistory{$string} = 1;
    push(@sendHistory, $string);
    
} # end sendHistoryGenList


sub clockstart {
    
    &sendMsg("ClockStart");

} # end clockstart


sub clockstop {
    
    &sendMsg("ClockStop");

} # end clockstop


sub clockswitch {
    
    if ($clockbackwardflag == 1) {
   &sendMsg("ClockBackward");
   $clockbackCheckbutton->configure(-foreground => 'maroon',
                                     -activeforeground => 'maroon');
    } else {
   &sendMsg("ClockForward");
   $clockbackCheckbutton->configure(-foreground => 'black',
                                     -activeforeground => 'black');
    }
    
} # end clockswitch


#----------------------------------------------------------------------------------
# Functions related to search panel
#----------------------------------------------------------------------------------
sub searchNextExpression {
    my $cursorIndex = $searchEntry->index('insert');
    $cursorIndex = -1 if $searchEntry->index('end') == $cursorIndex;
    if ($searchHistoryIndex == -1 or $searchHistoryIndex == @searchHistory - 1
           or @searchHistory <= 1) {
      &warning1($searchEntry);
    } else {
      $searchHistoryIndex++;
      $searchEntry->delete(0, 'end');
      $searchEntry->insert(0, $searchHistory[$searchHistoryIndex]);
      $searchEntry->icursor($cursorIndex) if $cursorIndex >= 0;
    }

} # end searchNextExpression


sub searchPrevExpression {
    my $cursorIndex = $searchEntry->index('insert');
    $cursorIndex = -1 if $searchEntry->index('end') == $cursorIndex;
    if ($searchHistoryIndex == 0 or @searchHistory <= 1) {
      &warning1($searchEntry);
    } else {
      if ($searchHistoryIndex == -1) {
          $searchHistoryIndex = @searchHistory - 2;
      } else {
          $searchHistoryIndex--;
      }
      $searchEntry->delete(0, 'end');
      $searchEntry->insert(0, $searchHistory[$searchHistoryIndex]);
      $searchEntry->icursor($cursorIndex) if $cursorIndex >= 0;
    }

} # end searchPrevExpression


sub clearSearch {
    #$searchEntry->delete(0, 'end');
    &highlightStringOff;
    $searchIndex = undef;
    $searchString = undef;

} # end clearSearch


sub searchHistoryGenList {
    my $string = shift;
    return if $searchHistory{$string};
    $searchHistory{$string} = 1;
    push(@searchHistory, $string);

} # end searchHistoryGenList

sub searchOnTheFly {
    my $string = $searchEntry->get;
    my $strlen;
    return unless $string;
    #print "string=$string\n";
    my $index0;
    if ($searchString) {
      if ($string ne $searchString) {
          $index0 = $searchIndex;
      } else {
          return;
      }
    } else {
      $index0 = '0.0';
    }
    &highlightStringOff;
    my @searchopts = (-hidden, -count => \$strlen, -forwards);
    push(@searchopts, -nocase) unless ($casesensitiveflag);
    push(@searchopts, -regexp) if ($regexpflag);
    my $index = $focusedtext->search(@searchopts, $string, $index0);
    #print "index=$index strlen=$strlen\n";
    if ($index) {
      &highlightStringOff if $searchString and $string eq $searchString;
      $searchString = $focusedtext->get($index, "$index + $strlen chars");
      $searchIndex = $index;
      &highlightString($index, "$index + $strlen chars");
      $focusedtext->see($index);
    } else {
      &warning2($focusedtext);
    }

} # end searchOnTheFly

sub searchNext {
    my $string = $searchEntry->get;
    my $strlen;
    return unless $string;
    my $index0 = ($searchIndex) ? "$searchIndex + 1 chars": '0.0';
    my @searchopts = (-hidden, -count => \$strlen, -forwards);
    push(@searchopts, -nocase) unless ($casesensitiveflag);
    push(@searchopts, -regexp) if ($regexpflag);
    my $index = $focusedtext->search(@searchopts, $string, $index0);
    #print "index=$index searchIndex=$searchIndex strlen=$strlen\n";
    &highlightStringOff;
    if ($index) {
      &highlightString($index, "$index + $strlen chars");
      $focusedtext->see($index);
      &warning1($focusedtext) if defined($searchIndex) and
          $focusedtext->compare($index, "<=" ,$searchIndex);
      $searchString = $focusedtext->get($index, "$index + $strlen chars");
      $searchIndex = $index;
    } else {
      &warning2($focusedtext);
    }
    $searchHistoryIndex = -1;
    &searchHistoryGenList($string);

} # end searchNext


sub searchPrev {
    my $string = $searchEntry->get;
    my $strlen ;
    return unless $string;
    #my $index0 = ($searchIndex) ? "$searchIndex - 1 chars": '0.0';
    my $index0 = ($searchIndex) ? $searchIndex : '0.0';
    my @searchopts = (-hidden, -count => \$strlen, -backwards);
    push(@searchopts, -nocase) unless ($casesensitiveflag);
    push(@searchopts, -regexp) if ($regexpflag);
    my $index = $focusedtext->search(@searchopts, $string, $index0);
    &highlightStringOff;
    if ($index) {
      &highlightString($index, "$index + $strlen chars");
      $focusedtext->see($index);
      &warning1($focusedtext) if defined($searchIndex) and
          $focusedtext->compare($index, ">=" ,$searchIndex);
      $searchString = $focusedtext->get($index, "$index + $strlen chars");
      $searchIndex = $index;
    } else {
      &warning2($focusedtext);
    }
    $searchHistoryIndex = -1;
    &searchHistoryGenList($string);

} # end searchPrev


sub searchAll {
    &highlightStringOff;
    my $string = $searchEntry->get;
    my $strlen;
    return unless $string;
    $focusedtext->tagConfigure('found',
                                 -background => 'sienna',
                                 -foreground => 'ivory');
    my $index = '0.0';
    my $found = 0;
    my @searchopts = (-hidden, -count => \$strlen, -forwards);
    push(@searchopts, -nocase) unless ($casesensitiveflag);
    push(@searchopts, -regexp) if ($regexpflag);

    while ($index) {
      $index = $focusedtext->search(@searchopts, $string, $index, 'end');
      last unless $index;
      $focusedtext->tagAdd('found', $index, "$index + $strlen chars");
      $index = "$index + 1 chars";
      $found++;
    }
    &warning2($focusedtext) unless ($found);
    $searchHistoryIndex = -1;
    &searchHistoryGenList($string);
    $searchString = undef;
    $searchIndex = undef;

} # end searchAll

#----------------------------------------------------------------------------------
# Functions related to control panel 
#----------------------------------------------------------------------------------
sub bye {
    if ($messagesNumber > 0) {
      my $diag = $mw->Dialog(-text =>
                               "Really quit Ivymon ?",
                               -default_button => 'OK',
                               -buttons => [qw(OK Cancel)]);
      my $answer = $diag->Show;
      if ($answer eq 'Cancel') {
          return;
      }
    }
    &quit;
} 

sub jump {
    my @markers =  $messagesText->markNames;
    my @markers2;
    for (@markers) {
      push(@markers2, $_) if (/^ivymon/);
    }
    if (@markers2 < $jump_cnt + 1) {
      $jump_cnt = 0;
    }
    $messagesText->yview($markers2[$jump_cnt]);
    $jump_cnt++;

} # end jump


sub stop {
    my $flag = shift;
    @messagesbuffer = ();
    $stopFlag = 1;
    $startButton->configure(-state => 'normal');
    $stopButton->configure(-state => 'disabled');

} # end stop


sub start {
    my $flag = shift;
    if ($messagesText->index('insert') !~ /\.0$/) {
      $messagesText->insert('insert', "\n");
    }
    
    # disable other buttons
    my $jumpstate = $jumpButton->cget(-state);
    $loadButton->configure(-state => 'disabled');
    $saveButton->configure(-state => 'disabled');
    $jumpButton->configure(-state => 'disabled');
    $clearButton->configure(-state => 'disabled');
    $startButton->configure(-state => 'disabled');
    $stopButton->configure(-state => 'disabled');
    $mw->update;

    $noUpdateFlag = 1;
    &beforeUpdatingMessages;
    for my $msg (@messagesbuffer) {
      &updateMessages($msg->[0], $msg->[1], $msg->[2]);
      $bufNumber--;
    }
    &afterUpdatingMessages;
    $noUpdateFlag = 0;
    $stopFlag = 0;

    # restore other buttons state
    $loadButton->configure(-state => 'normal');
    $saveButton->configure(-state => 'normal');
    $jumpButton->configure(-state => $jumpstate);
    $clearButton->configure(-state => 'normal');
    $stopButton->configure(-state => 'normal');

} # end start


sub clear {
    if ($messagesNumber > 0) {
      my $diag = $mw->Dialog(-text =>
                   "Do you really want to remove displayed messages ?",
                   -default_button => 'OK',
                   -buttons => [qw(OK Cancel)]);
      my $answer = $diag->Show;
      if ($answer eq 'Cancel') {
          return;
      }
    }

    # disable other buttons
    my $jumpstate = $jumpButton->cget(-state);
    my $startstate = $startButton->cget(-state);
    my $stopstate = $stopButton->cget(-state);
    $loadButton->configure(-state => 'disabled');
    $saveButton->configure(-state => 'disabled');
    $jumpButton->configure(-state => 'disabled');
    $clearButton->configure(-state => 'disabled');
    $startButton->configure(-state => 'disabled');
    $stopButton->configure(-state => 'disabled');
    $mw->update;

    $messagesText->configure(-state => 'normal');
    $messagesNumber = 0;
    $recordedNumber = 0;
    #$messagesText->delete('1.0', 'end');
    while ($messagesText->compare($messagesText->index('end'), ">", "2.0")) {
      $messagesText->delete('1.0', '1000.0');
      my $index = $messagesText->index('end');
    }
    $messagesText->configure(-state => 'disabled');

    # restore other buttons state
    $loadButton->configure(-state => 'normal');
    $saveButton->configure(-state => 'normal');
    $jumpButton->configure(-state => $jumpstate);
    $clearButton->configure(-state => 'normal');
    $startButton->configure(-state => $startstate);
    $stopButton->configure(-state => $stopstate);

} # end clear

#----------------------------------------------------------------------------------
# Functions related to input/output files
#----------------------------------------------------------------------------------
sub loadfile {

    my $file = $mw->getOpenFile(-filetypes => [['Ivy Files',   '.ivy'],
                      ['All Files',   '*']],
                );
    return unless $file;

    # open file
    unless (open(IN, "$file")) {
      $mw->Tk::Error("$!\n");
      return;
    }
    &showProgressbar();
    my $step = &stepsnumber();
    &loadfileForDisplay($step);
    &hideProgressbar();
    close(IN);
   
} # end loadfile


sub stepsnumber {

   my $step = 0;
   my $lc = 0;
   
   # get lines number and test if exists time field
   while(<IN>) {
      chomp;
      next if (/^applications=/ or /^(marker\d+)$/ or /^(messages_number=)/
               or /^\#/);
      $lc++;
   }
   $step = int($lc/10);
   $progressbar->configure(-to => $step*10);
   seek(IN, 0, 0);
   return $step;

} # end stepsnumber


sub loadfileForDisplay {

    my $step = shift;
    if ($messagesNumber > 0) {
      my $diag = $mw->Dialog(-text =>
                  "Some Ivy messages are already displayed. ".
                  "If you continue, loaded messages will ".
                  "be appended without distinction.\n",
                  -default_button => 'Continue',
                  -buttons => [qw(Continue Cancel)]);
      my $answer = $diag->Show;
      if ($answer eq 'Cancel') {
          return;
      }
    }

    # set load flags...
    $loadedFileFlag = 1;
    $loadingFlag = 1;
    $balloonhelp->attach($messagesMaxLabel, -balloonmsg =>
          "No history limit when file is loaded");
    $balloonhelp->attach($messagesDeletedLabel, -balloonmsg =>
          "Messages deletion is deactivated when file is loaded");

    $messagesDeletedLabel->configure(-foreground => 'gray60');
    $messagesDeletedValue->configure(-foreground => 'gray60');
    $messagesMaxLabel->configure(-foreground => 'gray60');
    # disable other buttons
    my $jumpstate = $jumpButton->cget(-state);
    my $startstate = $startButton->cget(-state);
    my $stopstate = $stopButton->cget(-state);
    $loadButton->configure(-state => 'disabled');
    $saveButton->configure(-state => 'disabled');
    $jumpButton->configure(-state => 'disabled');
    $clearButton->configure(-state => 'disabled');
    $startButton->configure(-state => 'disabled');
    $stopButton->configure(-state => 'disabled');
    $mw->update;

    my %client;
    my $line = 0;
    $noUpdateFlag = 1;
    &beforeUpdatingMessages;
    while(<IN>) {
      chomp;
      if (/^applications=/ or /^messages_number=/) {
          next;
      } elsif (/^\s*$/) {
          next;
          $line++;
      } elsif (/^(marker\d+)$/) {
          my $index = $messagesText->index('current linestart');
          &addmarker($index, $1);
      } else {
          my ($sender, $message) = split(/ /, $_, 2);
          my $time = undef;
          if ($message =~ /^(\d+)\s+(.*)/) {
            $time = $1;
            $message = $2;
          }
          unless ($client{$sender}) {
            $client{$sender} = 1;
            $clientsListbox->insert('end', $sender);
          }
          $messagesNumber++;
          $line++;
          #$message =~ s/^\"(.*)\"$/$1/;
          &loadMessage($sender, $message, $time);
          #print "sender=$sender message=$message step=$step line=$line\n";
          &setProgressbar($line, $step);
      }
    }
    &afterUpdatingMessages;
    $noUpdateFlag = 0;

    # restore other buttons state
    $loadButton->configure(-state => 'normal');
    $saveButton->configure(-state => 'normal');
    $jumpButton->configure(-state => $jumpstate);
    $clearButton->configure(-state => 'normal');
    $startButton->configure(-state => $startstate);
    $stopButton->configure(-state => $stopstate);
    $loadingFlag = 0;
    
} # end loadfileForDisplay





sub savefile {
    my ($d, $m, $y, $h, $M)  = (localtime(time))[3,4,5,2,1];
    $y =~ s/^\d// if $y >= 100;
    $m++;
    my $default = (defined $outputfile) ? $outputfile :
      sprintf("ivylog%02s%02s%02s_%02s:%02s.ivy", $d, $m, $y, $h, $M);
    my $file = $mw->getSaveFile(-initialfile => $default,
                               -filetypes => [['Ivy Files',   '.ivy'],
                                     ['All Files',   '*']],
                );
    return unless $file;
    $outputfile = $file;
    &showProgressbar();
    # disable other buttons
    my $jumpstate = $jumpButton->cget(-state);
    my $startstate = $startButton->cget(-state);
    my $stopstate = $stopButton->cget(-state);
    $loadButton->configure(-state => 'disabled');
    $saveButton->configure(-state => 'disabled');
    $jumpButton->configure(-state => 'disabled');
    $clearButton->configure(-state => 'disabled');
    $startButton->configure(-state => 'disabled');
    $stopButton->configure(-state => 'disabled');
    $mw->update;

    my $restorestate = sub {
      &hideProgressbar();
      # restore other buttons state
      $loadButton->configure(-state => 'normal');
      $saveButton->configure(-state => 'normal');
      $jumpButton->configure(-state => $jumpstate);
      $clearButton->configure(-state => 'normal');
      $startButton->configure(-state => $startstate);
      $stopButton->configure(-state => $stopstate);
    };
    
    my $status = 0;
    if (&save < 0) {
      $mw->Tk::Error("$!\n");
      &$restorestate;   
      return;
    }
    close(OUT);
    &$restorestate;

} # end savefile


sub openfile {
    
    my $file = $outputfile;
    $file = $opt_out unless defined $file;
    return 0 unless defined $file;
    if (open(OUT, ">$file")) {
      return 1;
    } else {
      close(OUT);
      $mw->Tk::messageBox(-icon => "warning",
                         -message => 
                         "Can't write to file $file ($!). ".
                         "Save data in ivymon-rescue.ivy",
                         -type => 'OK',
                         );   
      if (open(OUT, ">ivymon-rescue.ivy")) {
          return 1;
      } else {
          close(OUT);
          $mw->Tk::Error("Can't write to output file ($!)");
          return -1;
      }
    }

} # sub openfile


sub save {

    my $of = &openfile();
    return $of unless $of == 1;
    my $tpl = $mw->Toplevel;
    $tpl->Popup;
    $tpl->title("Save");
    $tpl->geometry("200x50");
    $tpl->Label(-text => "Saving data...")->pack(-expand => 1, -fill => 'both');
    # save connected applications name
    my @clients = $clientsListbox->get(0, 'end');
    print OUT "applications=", join(',', @clients),"\n" or return -1;

    my $nblines = $messagesText->index('end');
    #print "nblines=$nblines\n";
    $nblines =~ s/\.\d+$//;
    $nblines -= 2;
    $progressbar->configure(-to => $nblines);
    my $step = int($nblines/10);
    #print "step=$step\n";
    print OUT "messages_number=$nblines\n" or return -1;
    # save messages 100 by 100, in order to reduce memory usage
    my $index = "1.0";
    my $counter = 0;
    while(1) {
      my $messages = $messagesText->get($index, "$index + 100 lines");
      $index = "$index + 100 lines";
      unless ($messages) {
          &setProgressbar($nblines);
          $progressbar->update;
          last;
      }
      print OUT $messages or return -1;
      &setProgressbar($counter);
      $counter += 100;
      $progressbar->update if ($step == 0 or $counter % $step == 0);
    }
    &setProgressbar(0); 
    $mw->after(300, sub {$tpl->destroy;});
    $tpl->waitWindow;
    return 1;
    
} # end save


sub showProgressbar {

    $progressbar->value(0);
    $progressbar->toplevel->deiconify;
    $progressbar->toplevel->raise($mw);

} # end showProgressbar


sub hideProgressbar {
    
    $progressbar->toplevel->withdraw;
    
} # end hideProgressbar


sub setProgressbar {

    my ($line, $step) = @_;
    $progressbar->value($line);
    $progressbar->toplevel->raise($mw);
    return unless defined $step;
    $progressbar->update if ($step == 0 or $line % $step == 0);

} # end setProgressbar



#----------------------------------------------------------------------------------
# General functions
#----------------------------------------------------------------------------------

sub enlarge {
    
    @packinfo = ();
    push(@packinfo, [$bindings_fm->packInfo]);
    push(@packinfo, [$clients_fm->packInfo]);
    push(@packinfo, [$send_fm->packInfo]);
    push(@packinfo, [$searchandcontrol_fm->packInfo]);
    $searchandcontrol_fm->packForget;
    $clients_fm->packForget;
    $bindings_fm->packForget;

    
} # end expand


sub shrink {
    
    $send_fm->packForget;
    $bindings_fm->pack(@{$packinfo[0]});
    $clients_fm->pack(@{$packinfo[1]});
    $send_fm->pack(@{$packinfo[2]});
    $searchandcontrol_fm->pack(@{$packinfo[3]});
    
} # end shrink


sub wheelmousebindings {
    my $w = shift;
    my $count = shift;
    my $count = 3 unless $count > 0;
    $w->bind('<Control-ButtonPress-4>', sub {$w->yview('scroll', -1, 'page')});
    $w->bind('<Shift-ButtonPress-4>', sub {$w->yview('scroll', -1, 'unit')});
    $w->bind('<ButtonPress-4>', sub {$w->yview('scroll', -$count, 'unit')});
    
    $w->bind('<Control-ButtonPress-5>', sub {$w->yview('scroll', 1, 'page')});
    $w->bind('<Shift-ButtonPress-5>', sub {$w->yview('scroll', 1, 'unit')});
    $w->bind('<ButtonPress-5>', sub {$w->yview('scroll', $count, 'unit')});

} # end wheelmousebindings

sub findExprInList {
    my ($entry, $list) = @_;
    $recipientLabel->configure(-text => '');
    my $key = $entry->XEvent->K;
    my $expr = $entry->get;
    #print "expr=$expr\n";
    my @elems = $list->get(0, "end");
    # if key is Tab
    if ($key eq 'Tab') {
      my $index = 0;
      my @found;
      my $elem;
      my $qmexpr = quotemeta($expr);
      for $elem (sort @elems) {
          #print "qmexpr=$qmexpr qmelem=",quotemeta($elem),"\n";
          if ($expr and $elem =~ /^$qmexpr/) {
            push (@found, $index);
          }
          $index++;
      }
      # no expression found
      if (@found == 0) {
          &warning2($entry);
          my @cursel = $list->curselection;
          $list->selectionClear(@cursel) if @cursel > 0;
          &findRvalue($entry, $expr);
      # only one expression is found
      } elsif (@found == 1) {
          my @cursel = $list->curselection;
          $list->selectionClear(@cursel) if @cursel > 0;
          $list->selectionSet($found[0]);
          $list->see($found[0]);
          my $listexpr = $list->get($found[0]);
          if ($listexpr ne $expr) {
            $entry->delete(0, 'end');
            $entry->insert(0, scalar $list->get($found[0]));
          }
          &findRvalue($entry, $expr);
          
      # several expressions are found
      } else {
          &warning1($entry);
          my $i = 0;
          my $str = $list->get($found[0]);
          my $commonstr;
          for ($i=0; $i < length($str); $i++) {
            my $char = substr($str, length($expr)+$i, 1);
            my $j = 0;
            while ($j < @found) {
                last if substr(scalar $list->get($found[$j]), length($expr)+$i, 1)
                                    ne $char;
                $j++;
            }
            if ($j == @found) {
                $commonstr = $commonstr.$char;
            } else {
                last;
            }
          }
          $entry->insert('end', $commonstr);
      }
   
       # if entry is empty and if key is not Tab (key is backspace)
    } elsif (not $expr) {
         my @cursel = $list->curselection;
         $list->selectionClear(@cursel) if @cursel > 0;
      
       # if entry is not empty and key is not Tab
    } else {
      my $index = 0;
      for my $elem (sort @elems) {
          my $qwexpr = quotemeta($expr);
          if ($expr and $elem =~ /^$qwexpr/) {
            my @cursel = $list->curselection;
            $list->selectionClear(@cursel) if @cursel > 0;
            $list->selectionSet($index);
            $list->see($index);
            #print "$elem\n";
            last;
          }
          $index++;
      }
    }

} # end findExprInList


sub findRvalue {
    my $entry = shift;
    my $expr = shift;
    my $cursorIndex = $entry->index('insert');
    my $index = index($expr, '=', $cursorIndex);
    if ($index < 0) {
      $index = index($expr, '=');
    }
    return if $index < 0;
    my $rvalue = substr($expr, $index+1);
    $entry->selectionClear;
    if ($rvalue =~ /(^\S+)/) {
      $rvalue = $1;
      # skip char ) at the end of string 
      $rvalue =~ s/\)$//;
      $entry->selectionFrom($index+1);
      $entry->selectionTo($index +1 + length($rvalue));
    }
    $entry->icursor($index+1);
    $entry->xview($index-15);
    

} # end findRvalue


sub warning1 {
    my $widget = shift;
    $widget->configure(-background => 'gray90');
    $widget->after(100, sub {$widget->configure(-background => $bgcolor); });
    #$widget->bell;

} # end warning1


sub warning2 {
    my $widget = shift;
    $widget->configure(-background => 'gray30');
    $widget->after(100, sub {$widget->configure(-background => $bgcolor); });
    #$widget->bell;

} # end warning2


sub warning3 {
    my $widget = shift;
    $widget->configure(-foreground => 'red');
    $widget->after(100, sub {$widget->configure(-foreground => 'black')});
    
} # end warning3


sub quit {

    print "Quit\n";
    $mw->Tk::Error("$!\n") if &save < 0;
    exit;
    
} # end quit


__END__

=head1 NAME

ivymon - a graphical application for monitoring Ivy


=head1 SYNOPSIS

B<ivymon> [B<-b> ivybus] [B<-help>] [B<-history> size]
       [B<-size> window size] [B<-undersize>]
       [B<-bind> regexpB<1>] ... [B<-bind> regexpB<N>] 
       [B<-send> messageB<1>] ... [B<-send> messageB<N>]
       [B<-out> outputfile] [standard X11 options...]
       [INPUT-FILE]

=head1 DESCRIPTION

IvyMon is dedicated to monitor an Ivy bus. It prints out messages that match regular expressions, lets you send messages on bus and provides a keywords search interface.

The main area is the window labeled B<Messages> where are printed messages that forward on bus. Each message is preceded by sender application's name. You can insert a colored marker by double-clicking between two messages.

The B<Applications> area lists the connected applications names. When you select an item in the listbox, the messages sent by this application are highlighted. You can display the agent's bindings or kill an agent by using the dedicated buttons.

The B<Bindings> area is used to manage regular expressions to subscribe to ivy messages. It provides an input field to enter new regexp, a first listbox which contains an alphabetical list of available bindings, and a second listbox  of effective bindings. To bind a new regular expression, double-click on the corresponding item in the first list or select one, edit it in the input field and then hit the I<Return> key to validate. To remove subscription, double-click on the corresponding item in the second listbox. Pressing the I<Escape> key inserts I<(.*)> string in entry field. This field provides completion and history functionalities. The bindings of connected ivy agents are automaticalley added to the list. You can add your own bindings using the I<-bind> option.

The B<Messages to send> area is used to manage a list of predefined messages ready to be sent. It provides an input field to enter new message or edit existing one, and a listbox which contains an alphabetical list of available messages. When you validate an input (by pressing the I<Return> key), the new message is added to the listbox and sent on ivy bus. To send predefined messages, simply I<double-click> on corresponding item in the listbox. This field provides completion and history functionalities, and assistance for entering values. The application fits the bindings of connected ivy agents to build this list. You can add your own messages using the I<-send> option.

The B<Search> area provides an interface for searching pattern in messages window. It provides an input field to enter new pattern, and control buttons. To highlight all matches, press the I<All> button. To make incremental search, press the I<Next> button or the I<Return> key to proceed forward, and press the I<Previous> button or the I<Shift-Return> key to proceed backward. This field provides an history functionality.

The B<Control> panel contains some general control buttons. Set or unset balloon help functionality with the I<Balloon help> checkbutton. Stop the messages scrolling with the I<Scroll lock> button. You must do it if you want to insert a marker in the messages window. Restart scrolling with the I<Scroll> button. To access markers sequentially, press the I<Jump> button. With the I<Load> and I<Save> buttons, you can select input and output files. The I<Clear> button removes all displayed messages. At last, the I<Exit> button closes the application.


=head1 OPTIONS

=over

=item B<-b> ip:port

Set the bus domain and port number to be used. Use $IVYBUS variable if defined. Default is 127.255.255.255:2010.

=item B<-help>

Get some help

=item B<-history> size

Set the history size of messages window (default: 200000). If ivymon receives more messages, they won't be displayed neither stored. 

=item B<-size> window size

Set the size of the IvyMon window. Can be VGA or 640, SVGA or 800, XGA or 1024, SXGA or 1280, UXGA or 1600. Default is XGA (1024x768).

=item B<-undersize>

Slightly reduce the IvyMon window to fit it in screen with borders of the window manager. Option not set by default.

=item B<-bind> regular_expression

Subscribe to ivy messages, by using regular expression. This option may be used several times.

=item B<-send> ivy_message

Add an ivy message in the list of potential messages to send. This option may be used several times.

=item B<-out> output file

Specify the output filename and pass in storage mode : the content of the Messages area will be written to file when you exit the application. You can do manually the same using the B<Save> button. Note that output filename can be set or updated by sending the following message "Ivymon Output=<filename>".

=back


=head1 EXAMPLE

ivymon -b 10.192.36.255:3456 -history 20000 -bind 'PLN:(.*) SectorActivation sector=(.*)' -bind 'CLOCK start' -send 'TRAFFIC RadarEnd' -send 'AIRCRAFT: Activated' -size SVGA -undersize


=head1 HISTORY

B<V1.18, Jan 2007 :> a new command (ivyreplay) is dedicated to replay. ivymon does not provide this functionality anymore.

B<V1.16, Sep 2006 :> added a time regulation system for replay functionality

B<V1.8, Aug 2004 :> replay time granularity can be set.

B<V1.6, Jul 2004 :> a Replay mode is added with new command options to parameter it. Input file can be loaded by command argument. Messages storage can be activated at launching using the -out command option.

B<V1.4, Feb 2004 :> add a 'Filter' button to display the messages sent by an agent in a separate window.

B<V1.3, Nov 2003 :> size management, contributed by Cedric Mariot.

=head1 AUTHORS

Daniel Etienne <etienne@cena.fr>

Cedric Mariot <mariot@cena.fr> has contributed the code of size management.
