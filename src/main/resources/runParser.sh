#!/bin/bash

usage()
{
  echo "************************************************************************************************************************************************************"
  echo "Invalid parameters supplied. Please try again.                                                                                                              "
  echo "************************************************************************************************************************************************************"
  echo " Usage: ./runParser.sh "
  echo " -d     optional flag to produce verbose output if set "
  echo " -s     pattern specification parameter"
  echo " example ./runParser.sh -d -s \"pattern specification\""
  echo "************************************************************************************************************************************************************"
  exit 1
}

VERBOSE_MODE="false"
while test $# -gt 0; do
case ${1} in
    -s)
        shift
        SPECIFICATION="${1}"
        shift
    ;;
    -d)
        VERBOSE_MODE="true"
        shift
    ;;
    *)
        usage
    ;;
esac
done


#if [ -n ${SPECIFICATION} ]; then usage;  fi
if [ "${SPECIFICATION}" == "" ]; then usage;  fi

BASEDIR=$(dirname $BASH_SOURCE)
java \
    -cp ${BASEDIR}:${BASEDIR}/libs/${project.artifactId}-${project.version}-with-dependencies.jar \
    com.ninecy.app.ParserApp specification="${SPECIFICATION}" verbose=${VERBOSE_MODE}