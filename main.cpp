#include <iostream>

class Queen
{
public:
    Queen() { itsRow = 1 ; itsColumn = 1; }
    ~Queen(){}
    int GetRow() const { return itsRow; }
    int GetColumn() const { return itsColumn; }
    void SetRow(int row) { itsRow=row; }
    void SetColumn(int column) { itsColumn=column; }
    void IncrementSquare() {
        if (itsColumn<8) { itsColumn++; }
        else { if (itsRow<8) { itsRow++; itsColumn=1; }
        }
    }

private:
    int itsRow;
    int itsColumn;
};

using namespace std;

bool GetPosition(int i,int counter,Queen * Family);
void DisplayPosition(Queen * Family);
bool GetNextSquare(int i, Queen * Family);
bool notthreatens(Queen Queen1, Queen Queen2);

int main()
{
    int counter=0;
    Queen * Family = new Queen[8];
    int i=0;
    bool finished=false;
    while(1==1) {
    finished=GetPosition(i,counter,Family);
    if (finished) {break;}
    counter++; cout << counter << "\n"; DisplayPosition(Family);
    if (Family[7].GetRow()==8 && Family[7].GetColumn()==8) {
        i=6;
        Family[7].SetRow(1);
        Family[7].SetColumn(1);
        Family[6].IncrementSquare();}
    else {
        i=7;
        Family[7].IncrementSquare();
    }
    }
    return 0;
}

bool notthreatens (Queen Queen1, Queen Queen2)
{
    /* cout << "Comparing " << Queen1.GetRow() << " " <<
     Queen1.GetColumn() << " with " << Queen2.GetRow()<< " " << Queen2.GetColumn(); */
    if ((Queen1.GetRow()-Queen2.GetRow()==0) || (Queen1.GetColumn()-Queen2.GetColumn()==0)
        || (Queen1.GetRow()-Queen2.GetRow()==Queen1.GetColumn()-Queen2.GetColumn())
        || (Queen1.GetRow()-Queen2.GetRow()==Queen2.GetColumn()-Queen1.GetColumn()))
    {
        /* cout << "false\n"; */ return false;
    }
    else
    {
        /* cout << "true\n"; */ return true;
    }
}

bool GetNextSquare(int i, Queen * Family)
{
    // Get the next square for the i-th queen.
    /* cout << "i = " << i << "\n"; */
    int maxrow=1;
    int maxcolumn=1;
    for(int j=0; j<i; j++)
    {
        if (Family[j].GetRow()>maxrow) { maxrow = Family[j].GetRow(); maxcolumn = Family[j].GetColumn(); }
        if (Family[j].GetRow()==maxrow && Family[j].GetColumn()>maxcolumn) { maxcolumn = Family[j].GetColumn(); }
    }
    if (maxrow==8 && maxcolumn==8) { return true; }
    if (Family[i].GetRow()<maxrow || (Family[i].GetRow()==maxrow && Family[i].GetColumn()<maxcolumn)
        || ((Family[i].GetRow()==maxrow && Family[i].GetColumn()==maxcolumn) && i>0)) {
        Family[i].SetRow(maxrow);Family[i].SetColumn(maxcolumn);if (i>0) { Family[i].IncrementSquare(); }
    }
    /* cout << "row " << Family[i].GetRow() << " column " << Family[i].GetColumn() << "\n"; */
    bool onclearsquare=false;
    bool couldnt_construct=false;
        while(!onclearsquare)
        {
            onclearsquare=true;
            for (int j=0; j<i; j++)
            {
                onclearsquare=onclearsquare && notthreatens(Family[i],Family[j]);
            }
            if (!onclearsquare) {
                    if (Family[i].GetRow()==8 && Family[i].GetColumn()==8) {
                        couldnt_construct=true; break;
                    }
                    else {
                        Family[i].IncrementSquare();
                    }
            }
        }
        return couldnt_construct;
}

bool GetPosition(int i,int counter,Queen * Family)
{
    bool finished=false;
    bool couldnt_construct=false;
    bool displayer=false;
    while (i<8) {
       couldnt_construct = GetNextSquare(i,Family);
       /* if (counter==4 & i==0) { displayer=true; }
       if (displayer) { DisplayPosition(Family); } */
       if (couldnt_construct) {
            Family[i].SetRow(1);Family[i].SetColumn(1);
            i--;
            while (Family[i].GetRow()==8 && Family[i].GetColumn()==8) {
                if (i==0) { finished = true; break; }
                Family[i].SetRow(1); Family[i].SetColumn(1); i--;
            }
            if (i==0 && Family[i].GetRow()==8 && Family[i].GetColumn()==8) { finished = true; break; }
            Family[i].IncrementSquare(); }
       if (!couldnt_construct) { i++; }
    }
    return finished;
}

void DisplayPosition(Queen * Family){
    bool queen;
    int x;
    for (int i=0; i< 8; i++) {
        for (int j=0; j<8; j++) {
            queen = false;
            for (int k=0; k<8; k++) {
                if (Family[k].GetRow()==i+1 && Family[k].GetColumn()==j+1 && !queen && (i>0 || j>0 || k==0)) {
                    cout << "Q"; queen=true;
                }
            }
            if (!queen) { cout << "."; }
        }
        cout << "\n";
    }
    cin >> x;
}
