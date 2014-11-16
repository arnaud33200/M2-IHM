Public NotInheritable Class SplashScreen1

    'TODO: ce formulaire peut facilement être configuré comme écran de démarrage de l'application en accédant à l'onglet "Application"
    '  du Concepteur de projets ("Propriétés" sous le menu "Projet").

    Private f1 As Form1
    Private f2 As Form2
    Private f3 As Form3

    Private Sub SplashScreen1_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        'Configurez le texte de la boîte de dialogue au moment de l'exécution en fonction des informations d'assembly de l'application.  

        'TODO: personnalisez les informations d'assembly de l'application dans le volet "Application" de la 
        '  boîte de dialogue Propriétés du projet (sous le menu "Projet").

        'Titre de l'application
        If My.Application.Info.Title <> "" Then
            ApplicationTitle.Text = My.Application.Info.Title
        Else
            'Si le titre de l'application est absent, utilisez le nom de l'application, sans l'extension
            ApplicationTitle.Text = System.IO.Path.GetFileNameWithoutExtension(My.Application.Info.AssemblyName)
        End If

    End Sub

    Private Sub Button1_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Button1.Click
        Hide()
        f1 = New Form1
        f1.Visible = True
    End Sub
End Class
