Public NotInheritable Class SplashScreen1

    'TODO: ce formulaire peut facilement être configuré comme écran de démarrage de l'application en accédant à l'onglet "Application"
    '  du Concepteur de projets ("Propriétés" sous le menu "Projet").
    Enum FSTATE
        F1
        F2
        F3
    End Enum

    Private WithEvents f1 As Form1
    Private WithEvents f2 As Form2
    Private WithEvents f3 As Form3
    Private state As FSTATE

    Private Sub SplashScreen1_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load


        f1 = New Form1
        f1.Visible = False
        f2 = New Form2
        f2.Visible = False
        f3 = New Form3
        f3.Visible = False
    End Sub

    Public Sub f1_nextWindow() Handles f1.nextWindow
        Select Case state
            Case FSTATE.F1
                state = FSTATE.F2
                f1.Visible = False
                f2.Visible = True
                f3.Visible = False
            Case FSTATE.F2
                'INTERDIT
            Case FSTATE.F3
                'INTERDIT
        End Select
    End Sub

    Public Sub f2_nextWindow() Handles f2.nextWindow
        Select Case state
            Case FSTATE.F1
                'INTERDIT
            Case FSTATE.F2
                state = FSTATE.F3
                f1.Visible = False
                f2.Visible = False
                f3.Visible = True
            Case FSTATE.F3
                'INTERDIT
        End Select
    End Sub

    Public Sub f3_nextWindow() Handles f3.nextWindow
        Select Case state
            Case FSTATE.F1
                'INTERDIT
            Case FSTATE.F2
                'INTERDIT
            Case FSTATE.F3
                state = FSTATE.F1
                f1.Visible = True
                f2.Visible = False
                f3.Visible = False
        End Select
    End Sub

    Private Sub Timer1_Tick(sender As System.Object, e As System.EventArgs) Handles Timer1.Tick
        Timer1.Stop()
        Hide()
        f1.Visible = True
        state = FSTATE.F1
    End Sub
End Class
