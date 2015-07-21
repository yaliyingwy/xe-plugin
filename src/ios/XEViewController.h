//
//  XEViewController.h
//  xebest-base
//
//  Created by ywen on 15/7/20.
//
//

#import <Cordova/CDVViewController.h>
#import "XEPlugin.h"

@interface XEViewController : CDVViewController <XEProtocol, UIScrollViewDelegate>
@property (assign, nonatomic) id<ToastProtocol> xeToast;
@property (assign, nonatomic) id<LoadingProtocol> xeLoading;

@end
