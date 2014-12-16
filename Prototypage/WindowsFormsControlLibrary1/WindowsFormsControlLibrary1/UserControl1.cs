using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace WindowsFormsControlLibrary1
{
    public partial class UserControl1 : UserControl
    {
        Point lastPoint;
        enum Pos { L, M, R, N };
        Pos pointedPos = Pos.N;
        int minPos = 10;

        public int MinPos
        {
            get { return minPos; }
            set { minPos = value;
            if (minPos > maxPos - 40) minPos = maxPos - 40;
                label3.Text = "" + minPos;
                
                this.Refresh(); }
        }
        int maxPos = 200;

        public int MaxPos
        {
            get { return maxPos; }
            set { maxPos = value;
            if (maxPos < minPos + 40) maxPos = minPos + 40;
                label4.Text = "" + maxPos; 
                this.Refresh(); 
            }
        }
        
        public UserControl1()
        {
            InitializeComponent();
        }

        public delegate void RangeChanged(object sender, int min, int max);
        public event RangeChanged RangeChangedEvent;

        private void UserControl1_Paint(object sender, PaintEventArgs e)
        {
            Graphics g = e.Graphics;
            Brush b = Brushes.Black;
            if (pointedPos == Pos.M) b = Brushes.Chartreuse;
            if (pointedPos == Pos.L) b = Brushes.Brown;
            if (pointedPos == Pos.R) b = Brushes.DarkViolet;
            g.FillRectangle(b, minPos, 100, maxPos-minPos, 50);
            g.FillRectangle(Brushes.Brown, minPos, 100, 20, 50);
            g.FillRectangle(Brushes.DarkViolet, maxPos - 20, 100, 20, 50);
        }

        private void UserControl1_MouseDown(object sender, MouseEventArgs e)
        {
            if (e.X >= minPos && e.X <= (maxPos) &&
                e.Y >= 100 && e.Y <= 150)
            {
                pointedPos = Pos.M;
                if (e.X <= minPos + 20) pointedPos = Pos.L;
                if (e.X >= maxPos - 20) pointedPos = Pos.R;
                lastPoint = new Point(e.X, e.Y);
                this.Refresh();
            }
            else
            {
                pointedPos = Pos.N;
            }

        }



        private void UserControl1_MouseUp(object sender, MouseEventArgs e)
        {
            pointedPos = Pos.N;
            this.Refresh();
        }

        private void UserControl1_MouseMove(object sender, MouseEventArgs e)
        {
            if (pointedPos != Pos.N)
            {
                int dx = e.X - lastPoint.X;
                if (pointedPos == Pos.L) MinPos += dx;
                else if (pointedPos == Pos.R) MaxPos += dx;
                else
                {
                    MinPos += dx;
                    MaxPos += dx;
                }
                if (RangeChangedEvent != null) {
        RangeChangedEvent.Invoke(this,minPos,maxPos);
    }
                this.Refresh();
                lastPoint = new Point(e.X, e.Y);
            }
        }
    }
}
