//
//  XEPlugin.h
//  ios
//
//  Created by ywen on 15/7/1.
//  Copyright (c) 2015年 ywen. All rights reserved.
//

#import <Cordova/CDVPlugin.h>

typedef enum : NSUInteger {
    ToastBottom = 1,  //下
    ToastCenter = 2,  //中
    ToastTop = 3 //上
} ToastPosition;

@protocol XEProtocol <NSObject>

@required
-(void) commonCommand:(NSArray *) params;

@end

@protocol ToastProtocol <NSObject>

@required
-(void)showToastWithContent:(NSString *)content showTime:(NSTimeInterval)showTime postion:(ToastPosition) position;
-(void)showToastWithContent:(NSString *)content;
-(void) showSuccess:(NSString *) message;
-(void) showErr:(NSString *) message;
@end

@protocol LoadingProtocol <NSObject>

@required
-(void) show:(NSString *) message force:(BOOL) force;
-(void) hide;

@end

@interface XEPlugin : CDVPlugin

@property (assign, nonatomic) id<ToastProtocol> toast;
@property (assign, nonatomic) id<LoadingProtocol> loading;

-(void) normalCommand:(CDVInvokedUrlCommand *) command;
-(void) toast:(CDVInvokedUrlCommand *) command;
-(void) loading:(CDVInvokedUrlCommand *) command;

@end
