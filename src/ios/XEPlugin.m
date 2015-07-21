//
//  XEPlugin.m
//  ios
//
//  Created by ywen on 15/7/1.
//  Copyright (c) 2015年 ywen. All rights reserved.
//

#import "XEPlugin.h"

@implementation XEPlugin

-(void)normalCommand:(CDVInvokedUrlCommand *)command {
#ifdef DEBUG
    NSLog(@"command is %@", command.arguments);
#endif
    
    
    if ([self.viewController conformsToProtocol: @protocol(XEProtocol)]) {
        id <XEProtocol>  xe = (id <XEProtocol>)self.viewController;
        [xe commonCommand:command.arguments];
    }
}

-(void) toast:(CDVInvokedUrlCommand *)command {
    if (self.toast != nil) {
        NSString *message = @"";
        NSTimeInterval time = 1.5;
        ToastPosition position = ToastBottom;
        
        switch (command.arguments.count) {
            case 2:
                message = command.arguments[1];
                break;
                
            case 3:
                time = [command.arguments[2] doubleValue];
                break;
                
            case 4:
            {
                NSString *p = command.arguments[3];
                if ([p isEqualToString:@"top"]) {
                    position = ToastTop;
                }
                else if ([p isEqualToString:@"center"])
                {
                    position = ToastCenter;
                }
                else
                {
                    position = ToastBottom;
                }
                break;
            }
                
            default:
                break;
        }
        
        NSString *firstArg = command.arguments[0];
        if ([firstArg isEqualToString:@"show"]) {
            [self.toast showToastWithContent:message showTime:time postion:position];
        }
        else if ([firstArg isEqualToString:@"success"])
        {
            [self.toast showSuccess:message];
        }
        else if ([firstArg isEqualToString:@"error"])
        {
            [self.toast showErr:message];
        }
        
        
    }
    
}

-(void)loading:(CDVInvokedUrlCommand *)command {
    NSString *firstArg = command.arguments[0];
    
    if ([firstArg isEqualToString:@"show"]) {
        NSString *message = @"";
        BOOL isForce = YES;
        
        switch (command.arguments.count) {
            case 2:
                message = command.arguments[1];
                break;
                
            case 3:
            {
                
                isForce = [command.arguments[2] boolValue];
                break;
            }
                
            default:
                break;
        }
        
        [self.loading show:message force:isForce];
    }
    else
    {
        [self.loading hide];
    }
    
}

@end
