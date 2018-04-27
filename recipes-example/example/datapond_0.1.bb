SUMMARY = "Cognomotiv Datapond"
SECTION = "Cognomotiv"
LICENSE = "CLOSE"
LIC_FILES_CHKSUM = "file://README.md;md5=69e1f7117a36f9715662093c1d208128"
DEPENDS = "protobuf unzip zip  zlib libxml2 python3 python3-numpy python3-native"

SRC_URI = "git://git@github.com/Cognomotiv-Eng/Datapond.git;protocol=ssh;branch=master \
	  file://datapond.service \
	  file://init \
	"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

#inherit autotools pkgconfig setuptools python3native

inherit autotools update-rc.d systemd

INITSCRIPT_NAME = "datapond"
INITSCRIPT_PARAMS = "start 90 2 3 4 5 . stop 60 0 1 6 ."

SYSTEMD_SERVICE_${PN} = "datapond.service"


EXTRA_OEMAKE += 'CFLAGS="${CFLAGS} -I${STAGING_INCDIR}/python3.5m"'
do_compile() {
	cd "${S}"
	#make CFLAGS="-I${STAGING_INCDIR}/python3.5m"
	oe_runmake
}
do_install() {
	install -d ${D}${bindir}
        install -d ${D}${sysconfdir}/init.d
        install -d ${D}${systemd_unitdir}/system
	install -m 0755 ${S}/out/datapond ${D}${bindir}


        sed -e 's,/usr/bin,${bindir},g' ${WORKDIR}/init > ${D}${sysconfdir}/init.d/datapond
        chmod 755 ${D}${sysconfdir}/init.d/datapond

        install -m 0644 ${WORKDIR}/datapond.service ${D}${systemd_unitdir}/system
        sed -i -e 's,@BINDIR@,${bindir},g' ${D}${systemd_unitdir}/system/datapond.service



}

PACKAGES = "${PN} ${PN}-dev ${PN}-dbg"

FILES_${PN} += "${bindir}/collector"
FILES_${PN} += "${libdir}/libdatapondclient.so"
FILES_${PN} += "${libdir}/nullplugin.zip"


