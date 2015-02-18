Public Class Form1

    Public Event nextWindow()

    Private Sub Form1_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load

    End Sub

    Private Sub Button1_Click(sender As System.Object, e As System.EventArgs) Handles Button1.Click
        RaiseEvent nextWindow()
    End Sub

    Private Sub jej(sender As System.Object, e As System.EventArgs) Handles MyBase.FormClosing
        End
    End Sub

End Class
