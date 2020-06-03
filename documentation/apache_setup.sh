#On MAC OS X
#http://osxdaily.com/2012/09/02/start-apache-web-server-mac-os-x/
Launch Terminal, located in /Applications/Utilities/
Type the following command, replacing {username} with the user account short name:
sudo nano /etc/apache2/users/{username}.conf

Enter the admin password when requested, then paste the following into the nano text editor:
Alias "/pmsphotos" "/Users/{username}/Sites/pmsphotos"
<Directory "/Users/{username}/Sites/pmsphotos">
  Options Indexes Multiviews
  AllowOverride AuthConfig Limit
  Order allow,deny
  Allow from all
</Directory>

#start:
sudo apachectl start
#stop:
sudo apachectl stop
#restart:
sudo apachectl restart