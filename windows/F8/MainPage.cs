using ReactNative;
using ReactNative.Modules.Core;
using ReactNative.Shell;
using System.Collections.Generic;

namespace F8
{
    class MainPage : ReactPage
    {
        public override string MainComponentName
        {
            get
            {
                return "F8";
            }
        }

#if BUNDLE
        public override string JavaScriptBundleFile
        {
            get
            {
                return "ms-appx:///ReactAssets/index.windows.bundle";
            }
        }
#endif

        public override List<IReactPackage> Packages
        {
            get
            {
                return new List<IReactPackage>
                {
                    new MainReactPackage(),
                    new BV.LinearGradient.LinearGradientPackage(),
                };
            }
        }

        public override bool UseDeveloperSupport
        {
            get
            {
#if !BUNDLE || DEBUG
                return true;
#else
                return false;
#endif
            }
        }
    }

}
