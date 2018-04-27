SUMMARY = "Cognomotiv Collector Framework"
SECTION = "Cognomotiv"
LICENSE = "CLOSE"
LIC_FILES_CHKSUM = "file://README.md;md5=995655780dcd2d0574320f304aa469e5"
DEPENDS = "protobuf unzip zip zip-native zlib"

SRC_URI="git://git@github.com/Cognomotiv-Eng/CollectorFramework.git;protocol=ssh;branch=master \
	file://0001-Change-the-plugin-directory-to-opt-cognomotiv-pipeli.patch \
	file://collectfw.service \
	file://init \
	"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"
inherit autotools pkgconfig update-rc.d systemd

INITSCRIPT_NAME = "collectfw"
INITSCRIPT_PARAMS = "start 90 2 3 4 5 . stop 60 0 1 6 ."

SYSTEMD_SERVICE_${PN} = "collectfw.service"

do_compile() {
	#To resolve relocation error, uset CFLAGS
	unset CFLAGS
	cd "${S}"
	make
}
PLUGINS = "/opt/cognomotiv/pipeline/collector"
do_install() {
	install -d ${D}${bindir}
	install -d ${D}${libdir}
	install -d ${D}${PLUGINS}
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${systemd_unitdir}/system
	install -m 0755 ${S}/out/collector ${D}${bindir}
	install -m 0755 ${S}/out/libdatapondclient.so ${D}${libdir}
	cp -r  ${S}/out/plugin/ ${D}${PLUGINS}

	sed -e 's,/usr/bin,${bindir},g' ${WORKDIR}/init > ${D}${sysconfdir}/init.d/collectfw
        chmod 755 ${D}${sysconfdir}/init.d/collectfw

	install -m 0644 ${WORKDIR}/collectfw.service ${D}${systemd_unitdir}/system
	sed -i -e 's,@BINDIR@,${bindir},g' ${D}${systemd_unitdir}/system/collectfw.service

}
PACKAGES = "${PN} ${PN}-dev ${PN}-dbg"

FILES_${PN} += "${PLUGINS} \
		${bindir}/collector \
		${libdir}/libdatapondclient.so \
		${PLUGINS}/plugin/poll_nullplugin.zip \
		${PLUGINS}/plugin/tcpplugin.zip \
		${PLUGINS}/plugin/memplugin.zip \
		${PLUGINS}/plugin/cpuplugin.zip \
		${PLUGINS}/plugin/netplugin.zip \
		${PLUGINS}/plugin/push_nullplugin.zip \
		${PLUGINS}/plugin/tcpplugin/plugin.conf \
		${PLUGINS}/plugin/tcpplugin/tcpplugin.so \
		${PLUGINS}/plugin/push_nullplugin/plugin.conf \
		${PLUGINS}/plugin/push_nullplugin/push_nullplugin.so \
		${PLUGINS}/plugin/memplugin/plugin.conf \
		${PLUGINS}/plugin/memplugin/memplugin.so \
		${PLUGINS}/plugin/poll_nullplugin/plugin.conf \
		${PLUGINS}/plugin/poll_nullplugin/poll_nullplugin.so \
		${PLUGINS}/plugin/cpuplugin/plugin.conf \
		${PLUGINS}/plugin/cpuplugin/cpuplugin.so \
		${PLUGINS}/plugin/netplugin/netplugin.so \		
		${PLUGINS}/plugin/netplugin/plugin.conf \
	"
