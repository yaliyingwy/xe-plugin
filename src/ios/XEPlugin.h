//
//  XEPlugin.h
//  ios
//
//  Created by ywen on 15/7/1.
//  Copyright (c) 2015年 ywen. All rights reserved.
//

#import <Cordova/CDVPlugin.h>

typedef enum : NSUInteger {
    NavNext = 1,  //nav push
    NavBack = 2,  //nav pop
} CommandType;

@protocol XEProtocol <NSObject>

@required
-(void) navNext;
-(void) navBack;
-(void) commonCommand:(NSArray *) params;

@end

@interface XEPlugin : CDVPlugin

-(void) normalCommand:(CDVInvokedUrlCommand *) command;

@end
