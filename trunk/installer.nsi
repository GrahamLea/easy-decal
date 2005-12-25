; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "Easy Decal"
!define PRODUCT_VERSION "1.0"
!define PRODUCT_PUBLISHER "grlea.org"
!define PRODUCT_WEB_SITE "http://www.grlea.org/"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!define PRODUCT_STARTMENU_REGVAL "NSIS:StartMenuDir"

; MUI 1.67 compatible ------
!include "MUI.nsh"

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"

; Language Selection Dialog Settings
!define MUI_LANGDLL_REGISTRY_ROOT "${PRODUCT_UNINST_ROOT_KEY}"
!define MUI_LANGDLL_REGISTRY_KEY "${PRODUCT_UNINST_KEY}"
!define MUI_LANGDLL_REGISTRY_VALUENAME "NSIS:Language"

; Welcome page
!insertmacro MUI_PAGE_WELCOME
; License page
!insertmacro MUI_PAGE_LICENSE "release\installer\LICENSE.txt"
; Directory page
!insertmacro MUI_PAGE_DIRECTORY
; Start menu page
var ICONS_GROUP
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "Easy Decal"
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "${PRODUCT_UNINST_ROOT_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_KEY "${PRODUCT_UNINST_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "${PRODUCT_STARTMENU_REGVAL}"
!insertmacro MUI_PAGE_STARTMENU Application $ICONS_GROUP
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES
; Finish page
!define MUI_FINISHPAGE_RUN "$INSTDIR\EasyDecal.bat"
!define MUI_FINISHPAGE_SHOWREADME "$INSTDIR\README.txt"
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_LANGUAGE "French"
!insertmacro MUI_LANGUAGE "German"
!insertmacro MUI_LANGUAGE "Spanish"

; MUI end ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "EasyDecal_Setup.exe"
InstallDir "$PROGRAMFILES\Easy Decal"
ShowInstDetails show
ShowUnInstDetails show

Function .onInit
  !insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

Section "Easy Decal" SEC01
  SetOutPath "$INSTDIR"
  File "release\installer\ChangeLog.txt"
  SetOutPath "$INSTDIR\doc"
  File "release\installer\doc\Create_Decals.html"
  File "release\installer\doc\Install_Decals.html"
  File "release\installer\doc\style.css"
  SetOutPath "$INSTDIR"
  File "release\installer\easy-decal.cfg"
  File "release\installer\easy-decal.jar"
  File "release\installer\EasyDecal.bat"
  File "release\installer\EasyDecal.ico"
  File "release\installer\EasyDecal_Debug.bat"
  File "release\installer\jdic.dll"
  SetOutPath "$INSTDIR\lib\debug"
  File "release\installer\lib\debug\simplelog.properties"
  SetOutPath "$INSTDIR\lib"
  File "release\installer\lib\elcore.jar"
  File "release\installer\lib\explicitTableBuilder-0.1.24.jar"
  File "release\installer\lib\foxtrot.jar"
  File "release\installer\lib\gui-commands-1.1.42.jar"
  File "release\installer\lib\jdic-native.jar"
  File "release\installer\lib\jdic.jar"
  File "release\installer\lib\jdic_stub.jar"
  File "release\installer\lib\simple-log.jar"
  SetOutPath "$INSTDIR"
  File "release\installer\LICENSE.txt"
  File "release\installer\README.txt"
  File "release\installer\VERSION.txt"

; Shortcuts
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  CreateDirectory "$SMPROGRAMS\$ICONS_GROUP"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Easy Decal.lnk" "$INSTDIR\EasyDecal.bat" "" "$INSTDIR\EasyDecal.ico"
  CreateShortCut "$DESKTOP\Easy Decal.lnk" "$INSTDIR\EasyDecal.bat" "" "$INSTDIR\EasyDecal.ico"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Easy Decal (Debug).lnk" "$INSTDIR\EasyDecal_Debug.bat" "" "$INSTDIR\EasyDecal.ico"
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd

Section -AdditionalIcons
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  WriteIniStr "$INSTDIR\${PRODUCT_NAME}.url" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\www.grlea.org.lnk" "$INSTDIR\${PRODUCT_NAME}.url"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Uninstall.lnk" "$INSTDIR\uninst.exe"
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd

Section -Post
  WriteUninstaller "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
SectionEnd


Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) is history. Have a nice life.  ; )"
FunctionEnd

Function un.onInit
!insertmacro MUI_UNGETLANGUAGE
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "Do you want to get rid of  $(^Name)?" IDYES +2
  Abort
FunctionEnd

Section Uninstall
  !insertmacro MUI_STARTMENU_GETFOLDER "Application" $ICONS_GROUP
  Delete "$INSTDIR\${PRODUCT_NAME}.url"
  Delete "$INSTDIR\uninst.exe"
  Delete "$INSTDIR\VERSION.txt"
  Delete "$INSTDIR\README.txt"
  Delete "$INSTDIR\LICENSE.txt"
  Delete "$INSTDIR\lib\simple-log.jar"
  Delete "$INSTDIR\lib\jdic_stub.jar"
  Delete "$INSTDIR\lib\jdic.jar"
  Delete "$INSTDIR\lib\jdic-native.jar"
  Delete "$INSTDIR\lib\gui-commands-1.1.42.jar"
  Delete "$INSTDIR\lib\foxtrot.jar"
  Delete "$INSTDIR\lib\explicitTableBuilder-0.1.24.jar"
  Delete "$INSTDIR\lib\elcore.jar"
  Delete "$INSTDIR\lib\debug\simplelog.properties"
  Delete "$INSTDIR\jdic.dll"
  Delete "$INSTDIR\EasyDecal_Debug.bat"
  Delete "$INSTDIR\EasyDecal.ico"
  Delete "$INSTDIR\EasyDecal.bat"
  Delete "$INSTDIR\easy-decal.jar"
  Delete "$INSTDIR\easy-decal.cfg"
  Delete "$INSTDIR\doc\style.css"
  Delete "$INSTDIR\doc\Install_Decals.html"
  Delete "$INSTDIR\doc\Create_Decals.html"
  Delete "$INSTDIR\ChangeLog.txt"

  Delete "$SMPROGRAMS\$ICONS_GROUP\Uninstall.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\Website.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\Easy Decal (Debug).lnk"
  Delete "$DESKTOP\Easy Decal.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\Easy Decal.lnk"

  RMDir "$SMPROGRAMS\$ICONS_GROUP"
  RMDir "$INSTDIR\lib\debug"
  RMDir "$INSTDIR\lib"
  RMDir "$INSTDIR\doc"
  RMDir "$INSTDIR"

  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  SetAutoClose true
SectionEnd