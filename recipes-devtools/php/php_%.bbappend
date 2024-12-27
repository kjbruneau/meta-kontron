PACKAGECONFIG:append = " zip gd"
PACKAGECONFIG[gd] = "--enable-gd \
                     --with-xpm \
                     ,,libxpm libpng"
