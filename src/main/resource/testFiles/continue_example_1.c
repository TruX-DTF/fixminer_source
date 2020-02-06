while ( i-- > 1 )
{
    x = f( i );
    if ( x == 0 ) {
        continue;
    }
    y += x * x;
}